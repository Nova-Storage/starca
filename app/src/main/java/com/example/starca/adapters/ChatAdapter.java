package com.example.starca.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.starca.R;
import com.example.starca.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starca.models.Conversation;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

   private static final int MESSAGE_OUTGOING = 123;
   private static final int MESSAGE_INCOMING = 321;

   @Override
   public int getItemViewType(int position) {
      if (isMe(position)) {
         return MESSAGE_OUTGOING;
      } else {
         return MESSAGE_INCOMING;
      }
   }

   private List<Message> mMessages;
   private Context mContext;
   private String mUserId;
   private Conversation mConversation;

   public ChatAdapter(Context context, String userId, ArrayList<Message> messages, Conversation conversation) {
      mMessages = messages;
      this.mUserId = userId;
      Log.d("ChatAdapter", this.mUserId);
      mContext = context;
      mConversation = conversation;
   }

   @Override
   public int getItemCount() {
      return mMessages.size();
   }



   private boolean isMe(int position) {
      Message message = mMessages.get(position);
      return message.getUserId() != null && message.getUserId().equals(mUserId);
   }

   @Override
   public void onBindViewHolder(MessageViewHolder holder, int position) {
      Message message = mMessages.get(position);
      holder.bindMessage(message);
   }

   public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

      public MessageViewHolder(@NonNull View itemView) {
         super(itemView);
      }

      abstract void bindMessage(Message message);
   }

   public class IncomingMessageViewHolder extends MessageViewHolder {
      ImageView imageOther;
      TextView body;

      public IncomingMessageViewHolder(View itemView) {
         super(itemView);
         imageOther = (ImageView) itemView.findViewById(R.id.ivProfileOther);
         body = (TextView) itemView.findViewById(R.id.tvBody);
      }

      @Override
      public void bindMessage(Message message) {
         Glide.with(mContext)
                 .load(Objects.requireNonNull(mConversation.getYou().getParseFile("profilePicture")).getUrl())
                 .circleCrop() // create an effect of a round profile picture
                 .into(imageOther);
         body.setText(message.getBody());
        //name.setText(message); // in addition to message show user ID
      }
   }

   public class OutgoingMessageViewHolder extends MessageViewHolder {
      ImageView imageMe;
      TextView body;

      public OutgoingMessageViewHolder(View itemView) {
         super(itemView);
         imageMe = (ImageView) itemView.findViewById(R.id.ivProfileMe);
         body = (TextView) itemView.findViewById(R.id.tvBody);
      }

      @Override
      public void bindMessage(Message message) {
         Glide.with(mContext)
                 .load(Objects.requireNonNull(mConversation.getOtherPerson().getParseFile("profilePicture")).getUrl())
                 .circleCrop() // create an effect of a round profile picture
                 .into(imageMe);
         body.setText(message.getBody());
      }
   }

   @Override
   public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);

      if (viewType == MESSAGE_INCOMING) {
         View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
         return new IncomingMessageViewHolder(contactView);
      } else if (viewType == MESSAGE_OUTGOING) {
         View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
         return new OutgoingMessageViewHolder(contactView);
      } else {
         throw new IllegalArgumentException("Unknown view type");
      }
   }
}