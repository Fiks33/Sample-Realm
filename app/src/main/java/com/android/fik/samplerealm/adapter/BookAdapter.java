package com.android.fik.samplerealm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fik.samplerealm.R;
import com.android.fik.samplerealm.activity.MainActivity;
import com.android.fik.samplerealm.model.Book;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Mochamad Taufik on 09-Nov-16.
 * Email   : thidayat13@gmail.com
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Book> arrlist;
    private String search = null;

    private static Context context;
    private Realm mRealm;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_book, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    public void setArray(List<Book> dList) {
        this.arrlist = dList;
    }

    public BookAdapter(Context c) {
        context = c;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        mRealm              = Realm.getDefaultInstance();

        TextView mBook      = (TextView) holder.itemView.findViewById(R.id.book);
        TextView mAuthor    = (TextView) holder.itemView.findViewById(R.id.author);

        mBook.setText(arrlist.get(position).getBook());
        mAuthor.setText("Author : "+ arrlist.get(position).getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertPrinter =
                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                        R.layout.item_list_action);

                arrayAdapter.add("Ubah");
                arrayAdapter.add("Hapus");

                alertPrinter.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                        if (strName.equals("Ubah")) {

                            if(context instanceof MainActivity){
                                ((MainActivity)context).UpdateData(arrlist.get(position).getId(),
                                        arrlist.get(position).getBook(),arrlist.get(position).getAuthor());
                            }
                        } else {

                            final RealmResults<Book> BookRealm = mRealm.where(Book.class).findAll();
                            Book books = BookRealm.where().equalTo("id",arrlist.get(position).getId()).findFirst();
                            if(books != null){
                                if (!mRealm.isInTransaction()) {
                                    mRealm.beginTransaction();
                                }
                                books.deleteFromRealm();
                                mRealm.commitTransaction();
                                removeAt(position);

                                Toast.makeText(context,"Terhapus",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                alertPrinter.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrlist.size();
    }

    public void removeAt(int position) {
        arrlist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrlist.size());
    }
}
