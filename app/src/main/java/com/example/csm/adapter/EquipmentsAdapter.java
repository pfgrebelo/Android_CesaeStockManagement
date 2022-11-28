package com.example.csm.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csm.model.Equipments;
import com.example.csm.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EquipmentsAdapter extends FirestoreRecyclerAdapter<Equipments, EquipmentsAdapter.EquipmentsHolder> {

    private OnItemClickListener listener;
    private Uri uri_image;

    public EquipmentsAdapter(@NonNull FirestoreRecyclerOptions<Equipments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EquipmentsHolder holder, int position, @NonNull Equipments model) {
        holder.tv_nameEquip.setText(model.getName());
        holder.tv_brandEquip.setText(model.getBrand());
        /// Converte e envia a imagem para circleimageview ///
        uri_image = Uri.parse(model.getPhoto());
        if (!uri_image.toString().isEmpty()) {
            Picasso.get().load(uri_image).into(holder.civ_photoEquip);
        } else {
            holder.civ_photoEquip.setImageResource(R.drawable.logo_cesae_icon);
        }
    }

    @NonNull
    @Override
    public EquipmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.equip_item,parent,false);
        return new EquipmentsHolder(v);
    }

    class EquipmentsHolder extends RecyclerView.ViewHolder {

        TextView tv_nameEquip, tv_brandEquip;
        CircleImageView civ_photoEquip;

        public EquipmentsHolder(@NonNull View itemView) {
            super(itemView);

            tv_nameEquip = itemView.findViewById(R.id.tv_nameEquip);
            tv_brandEquip = itemView.findViewById(R.id.tv_brandEquip);


            civ_photoEquip = itemView.findViewById(R.id.civ_photoEquip);

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
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

