package com.example.csm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csm.R;
import com.example.csm.model.Rooms;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RoomsAdapter extends FirestoreRecyclerAdapter<Rooms,RoomsAdapter.RoomsHolder> {

    private OnItemClickListener listener;

    public RoomsAdapter(@NonNull FirestoreRecyclerOptions<Rooms> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RoomsHolder holder, int position, @NonNull Rooms model) {

        holder.tv_nameRoom.setText(model.getRoomName());
        holder.tv_descRoom.setText(model.getDesignation());
    }

    @NonNull
    @Override
    public RoomsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item,parent,false);
        return new RoomsHolder(v);
    }

    class RoomsHolder extends RecyclerView.ViewHolder{

        TextView tv_nameRoom, tv_descRoom;

        public RoomsHolder(@NonNull View itemView) {
            super(itemView);

            tv_nameRoom = itemView.findViewById(R.id.tv_nameRoom);
            tv_descRoom = itemView.findViewById(R.id.tv_descRoom);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION &&listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(pos),pos);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int pos);
    }
    public void setOnItemClickListener(RoomsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
