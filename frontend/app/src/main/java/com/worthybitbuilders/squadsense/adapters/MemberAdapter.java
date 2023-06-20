package com.worthybitbuilders.squadsense.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.models.UserModel;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter {
    private final List<UserModel> memberList;
    private OnActionCallback callback;

    public interface OnActionCallback {
        void OnClick(int position);
    }

    public MemberAdapter(List<UserModel> memberList) {
        this.memberList = memberList;
    }

    public void setOnClickListener(OnActionCallback callback) {
        this.callback = callback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_member, parent, false);
            return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel member = (UserModel) memberList.get(position);
        ((MemberAdapter.MemberHolder) holder).bind(member, position);
    }


    @Override
    public int getItemCount() {
        return memberList.size();
    }

    private class MemberHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName;
        ImageView memberAvatar;
        MemberHolder(View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.member_name);
            memberAvatar = itemView.findViewById(R.id.member_avatar);
        }

        void bind(UserModel member, int position) {
            tvMemberName.setText(member.getName());

            Glide.with(itemView.getContext())
                    .load(member.getProfileImagePath())
                    .placeholder(R.drawable.ic_user)
                    .into(memberAvatar);

            itemView.setOnClickListener(view -> callback.OnClick(position));
        }
    }
}