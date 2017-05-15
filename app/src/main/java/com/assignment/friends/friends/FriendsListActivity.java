package com.assignment.friends.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.assignment.friends.friends.model.Profile;
import com.assignment.friends.friends.util.HttpGetAsyncTask;
import com.assignment.friends.friends.util.JsonHandler;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.InputStream;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    //private List<ApplicationInfo> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    protected List<Profile> friendsList;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //id = intent.getIntExtra("id",0);
        id = 3;
        if(id != 0) {
            String path = "http://192.168.191.1:8080/FindFriends/webresources/friendship/" + id;
            GetListTask getTask = new GetListTask(this, "failed to get friends list", path);
            getTask.execute();
        }

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
                        R.layout.friend_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Profile item = getItem(position);
            holder.iv_icon.setImageDrawable(getDrawable(R.mipmap.male4));
            holder.tv_name.setText(item.getFirstName()+" "+item.getSurname());
            holder.tv_email.setText(item.getEmail());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_email;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_email = (TextView) view.findViewById(R.id.email);
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

    class GetListTask extends HttpGetAsyncTask<Void,List<Profile>>{

        public GetListTask(Context c, String msg, String path) {
            super(c, msg, path);
        }

        @Override
        protected void httpPostExecute(final List<Profile> profiles) throws Exception {
            if (profiles.size() == 0){
                Toast.makeText(getApplicationContext(),"Your friend list is empty. \nGo to find friends!",Toast.LENGTH_SHORT).show();
            }else{
                friendsList = profiles;
                mAdapter = new AppAdapter();
                mListView = (SwipeMenuListView)findViewById(R.id.friends_list_view);
                mListView.setAdapter(mAdapter);

                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        // create "open" item
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                                0xCE)));
                        // set item width
                        openItem.setWidth(dp2px(90));
                        // set item title
                        openItem.setTitle("Open");
                        // set item title font size
                        openItem.setTitleSize(18);
                        // set item title font color
                        openItem.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(openItem);

                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        // set item width
                        deleteItem.setWidth(dp2px(90));
                        // set a icon
                        deleteItem.setIcon(R.drawable.ic_delete);
                        // add to menu
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
                                // delete
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendsListActivity.this);
                                    AlertDialog alert = builder.setIcon(R.mipmap.own_ic_launcher)
                                            .setTitle("Attention")
                                            .setMessage("Are you sure to delete this friends?")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int id1 = friendsList.get(position).getId();
                                                    String path = "http://192.168.191.1:8080/FindFriends/webresources/friendship/del/" + id + "/" + id1;
                                                    DeleteFriendTask deleteTask = new DeleteFriendTask(getApplicationContext(), "failed to delete this friends", path, position);
                                                    deleteTask.execute();
                                                }
                                            }).create();
                                    alert.show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
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

    class DeleteFriendTask extends HttpGetAsyncTask<Void,Integer>{
        private int mPosition;

        public DeleteFriendTask(Context c, String msg, String path, int pos) {
            super(c, msg, path);
            this.mPosition = pos;
        }

        @Override
        protected void httpPostExecute(Integer id) throws Exception {
            if (id == 0)
                throw new Exception("returned 0");
            else{
                friendsList.remove(mPosition);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Delete Successfully!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Integer ownTask(InputStream result) throws Exception {
            String json = JsonHandler.readInputStream(result);

            return Integer.parseInt(json);
        }
    }

}
