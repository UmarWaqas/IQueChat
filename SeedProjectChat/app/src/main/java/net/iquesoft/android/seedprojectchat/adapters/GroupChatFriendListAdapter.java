package net.iquesoft.android.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import net.iquesoft.android.seedprojectchat.model.ChatUser;
import net.iquesoft.android.seedprojectchat.model.Friends;

import java.util.List;

public class GroupChatFriendListAdapter extends RecyclerView.Adapter<GroupChatFriendListAdapter.ViewHolder> {

    private List<Friends> friendsList;
    private Context context;

    public GroupChatFriendListAdapter(List<Friends> friendsList, Context context){
        this.friendsList = friendsList;
        this.context = context;
    }

    @Override
    public GroupChatFriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.create_group_chat_friends_raw, parent, false);
        return new GroupChatFriendListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupChatFriendListAdapter.ViewHolder holder, int position) {
        Friends myFriends = friendsList.get(position);
        String logginedUserId = Backendless.UserService.CurrentUser().getUserId();
        BackendlessUser user;
        if (logginedUserId.equals(myFriends.getUser_one().getUserId())){
            user = myFriends.getUser_two();
        } else {
            user = myFriends.getUser_one();
        }
        try {
            Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
            Picasso.with(context).load(uri).placeholder(net.iquesoft.android.seedprojectchat.R.drawable.seed_logo).error(net.iquesoft.android.seedprojectchat.R.drawable.button_bacground_log_in_screen).into(holder.cimUserImage);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
            Boolean online = (Boolean) user.getProperty(ChatUser.ONLINE);
            if (online){
                holder.online.setImageDrawable(context.getResources().getDrawable(net.iquesoft.android.seedprojectchat.R.drawable.online));
            } else {
                holder.online.setImageDrawable(context.getResources().getDrawable(net.iquesoft.android.seedprojectchat.R.drawable.offline));
            }
            holder.tvUserName.setText(user.getProperty(ChatUser.NAME).toString());
            holder.tvUserEMail.setText(user.getEmail());
            myFriends.setSelected(holder.isChecked.isSelected());
            holder.isChecked.setOnCheckedChangeListener((buttonView, isChecked) -> myFriends.setSelected(isChecked));

    }

    public List<Friends> getFriendsList(){
        return friendsList;
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        CircularImageView online;
        CheckBox isChecked;
        TextView tvUserName;
        TextView tvUserEMail;

        public ViewHolder(View itemView) {
            super(itemView);
            cimUserImage = (CircularImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.cim_user_image);
            online = (CircularImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.cim_online);
            tvUserEMail = (TextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.tv_last_message);
            tvUserName = (TextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.tv_group_chat_name);
            isChecked = (CheckBox) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.is_selected);
        }
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        friendsList.remove(index);
        notifyItemRemoved(index);
    }
}
