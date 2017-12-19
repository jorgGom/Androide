package com.example.jorge.agenda.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.providers.EventsContract;

import java.util.Random;

/**
 * Created by Jorge on 10/12/2017.
 */

public class NotasCursorAdapter extends RecyclerView.Adapter<NotasCursorAdapter.VH>{
    private Cursor mCursor;
    private AlClicarNoItem mListener;
    private Random mRandom = new Random();


    public NotasCursorAdapter(AlClicarNoItem Listener) {
       mListener = Listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid_layout, parent, false);

        final VH vh = new VH(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                mCursor.moveToPosition(position);
                if (mListener != null) mListener.itemFueClicado(mCursor);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mCursor.moveToPosition(position);
        //int idx_titulo = mCursor.getColumnIndex(EventsContract.Columnas.TITULO);
        int idx_descr = mCursor.getColumnIndex(EventsContract.Columnas.DESCRIPCION);



        //String titulo = mCursor.getString(idx_titulo);
        String descripcion = mCursor.getString(idx_descr);

        holder.cardView.setBackgroundColor(getRandomHSVColor());


       // holder.titulo.setText(titulo);
        holder.descripcion.setText(descripcion);
        holder.descripcion.setTextColor(Color.BLACK);

    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                int idx_id = mCursor.getColumnIndex(EventsContract.Columnas._ID);
                return mCursor.getLong(idx_id);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public void setCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface AlClicarNoItem {
        void itemFueClicado(Cursor cursor);
    }

    public class VH extends RecyclerView.ViewHolder {
        public TextView titulo,descripcion;
        public CardView cardView;

        public VH(View v) {
               super(v);
                //titulo = (TextView) v.findViewById(R.id.tituloRec);
            descripcion = (TextView) v.findViewById(R.id.descripcionRec);
            cardView = (CardView) v.findViewById(R.id.cardV);

            }
        }

    protected int getRandomHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        // We make the color depth full
        float saturation = 0.4f;
        // We make a full bright color
        float value = 1.0f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }
    }

