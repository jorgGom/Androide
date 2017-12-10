package com.example.jorge.agenda.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.providers.EventsContract;

/**
 * Created by Jorge on 10/12/2017.
 */

public class EventsCursorAdapter extends RecyclerView.Adapter<EventsCursorAdapter.VH>{
    private Cursor mCursor;
    private AlClicarNoItem mListener;

    public EventsCursorAdapter(AlClicarNoItem Listener) {
       mListener = Listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

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
        int idx_titulo = mCursor.getColumnIndex(EventsContract.Columnas.TITULO);
        int idx_descr = mCursor.getColumnIndex(EventsContract.Columnas.DESCRIPCION);
        int idx_fecha = mCursor.getColumnIndex(EventsContract.Columnas.FECHA_EVENTO);
        int idx_hora = mCursor.getColumnIndex(EventsContract.Columnas.HORA_EVENTO);


        String titulo = mCursor.getString(idx_titulo);
        String descripcion = mCursor.getString(idx_descr);
        String fecha = mCursor.getString(idx_fecha);
        String hora = mCursor.getString(idx_hora);

        holder.titulo.setText(titulo);
        holder.descripcion.setText(descripcion);
        holder.fecha.setText(fecha);
        holder.hora.setText(hora);
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
        public TextView titulo, descripcion, fecha, hora ;

        public VH(View v) {
               super(v);
                titulo = (TextView) v.findViewById(R.id.TituloTV1);
                descripcion = (TextView) v.findViewById(R.id.DescripcionTV1);
                fecha = (TextView) v.findViewById(R.id.FechaTV1);
                hora = (TextView) v.findViewById(R.id.HoraTV1);
            }
        }
    }

