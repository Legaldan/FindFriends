package com.assignment.friends.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.assignment.friends.friends.model.Friendship;
import com.assignment.friends.friends.model.Profile;
import com.assignment.friends.friends.util.HttpGetAsyncTask;
import com.assignment.friends.friends.util.HttpPostAsyncTask;
import com.assignment.friends.friends.util.JsonHandler;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class SearchFriendsActivity extends AppCompatActivity {

    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    protected List<Profile> friendsList;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getInt("id",0);

        String path = "http://192.168.191.1:8080/FindFriends/webresources/profile/";
        GetListTask getTask = new GetListTask(this, "failed to get friends list", path);
        getTask.execute();
    }


    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return friendsList.size();
        }

        @Override
        public Profile getItem(int position) {
            return friendsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.serach_friend_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Profile item = getItem(position);
            int index = (int)item.getFirstName().charAt(0)%6;
            String fileName = "ic_header_"+index;
            int id =getResources().getIdentifier(fileName,"drawable",getApplicationContext().getPackageName());
            holder.iv_icon.setImageResource(id);
            holder.tv_first_name.setText(item.getFirstName());
            holder.tv_surname.setText(item.getSurname());
            holder.tv_movie.setText(item.getFavMovie());
            holder.tv_sport.setText(item.getFavSport());
            holder.tv_gender.setText(item.getGender() == 0 ? "Male" : "Female");
            holder.tv_nation.setText(item.getNationality());
            holder.tv_birth.setText(item.getBirth().substring(0,10));
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_first_name;
            TextView tv_surname;
            TextView tv_movie;
            TextView tv_sport;
            TextView tv_gender;
            TextView tv_nation;
            TextView tv_birth;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.imageView);
                tv_first_name = (TextView) view.findViewById(R.id.first_name);
                tv_surname = (TextView) view.findViewById(R.id.surname);
                tv_birth = (TextView) view.findViewById(R.id.birth);
                tv_nation = (TextView) view.findViewById(R.id.nationality);
                tv_gender = (TextView) view.findViewById(R.id.gender);
                tv_sport = (TextView) view.findViewById(R.id.favorite_sport);
                tv_movie = (TextView) view.findViewById(R.id.favorite_movie);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    protected void openProfile(Profile profile){
        Intent intent = new Intent(this,ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile",profile);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    class GetListTask extends HttpGetAsyncTask<Void,List<Profile>> {

        public GetListTask(Context c, String msg, String path) {
            super(c, msg, path);
        }

        @Override
        protected void httpPostExecute(final List<Profile> profiles) throws Exception {
            if (profiles.size() == 0){
                Toast.makeText(getApplicationContext(),"Find Nobody",Toast.LENGTH_SHORT).show();
            }else{
                friendsList = profiles;
                mAdapter = new AppAdapter();
                mListView = (SwipeMenuListView)findViewById(R.id.friends_list_view);
                mListView.setAdapter(mAdapter);

                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                                0xCE)));
                        openItem.setWidth(dp2px(90));
                        openItem.setTitle("Open");
                        openItem.setTitleSize(18);
                        openItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(openItem);

                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        deleteItem.setWidth(dp2px(90));
                        deleteItem.setIcon(R.drawable.ic_add_friend);
                        menu.addMenuItem(deleteItem);
                    }
                };

                // set creator
                mListView.setMenuCreator(creator);

                mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                // open
                                openProfile(friendsList.get(position));
                                break;
                            case 1:
                                String path = "http://192.168.191.1:8080/FindFriends/webresources/friendship/";
                                AddFriendTask addTask = new AddFriendTask(getApplicationContext(),"add new friend failed",path,friendsList.get(position).getId());
                                addTask.execute();
                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });

                // Right
                mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            }
        }

        @Override
        protected List<Profile> ownTask(InputStream result) throws Exception {
            String json = JsonHandler.readInputStream(result);
            List<Profile> profileList = JSON.parseArray(json,Profile.class);
            return profileList;
        }
    }


    class AddFriendTask extends HttpPostAsyncTask<Integer,Void>{
        int stu2Id;

        public AddFriendTask(Context c, String msg, String path, int p) {
            super(c, msg, path);
            stu2Id = p;
        }

        @Override
        protected void httpPostExecute(Void aVoid) throws Exception {
            Toast.makeText(context,"Add successfully",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void ownTask(OutputStream os) throws Exception {

            Friendship friendship = new Friendship(new Date(),null,id,stu2Id);

            String content = JSON.toJSONString(friendship);
            os.write(content.getBytes());
            os.close();
            int code = conn.getResponseCode();
            if (code < 300){
                //ready to get response from server
                return null;
            }else{
                throw new Exception();
            }
        }
    }
}
