package com.android.fik.samplerealm.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.fik.samplerealm.R;
import com.android.fik.samplerealm.adapter.BookAdapter;
import com.android.fik.samplerealm.model.Book;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by Mochamad Taufik on 08-Nov-16.
 * Email   : thidayat13@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private EditText mBook, mAuthor;
    private Realm mRealm;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<Book> mListBook;

    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar    = (Toolbar) findViewById(R.id.toolbar);
        mFab        = (FloatingActionButton) findViewById(R.id.fab);
        mRecycler   = (RecyclerView) findViewById(R.id.recycler);

        setSupportActionBar(mToolbar);
        setupActionBar();

        mListBook           = new ArrayList<>();
        mAdapter            = new BookAdapter(this);
        mLayoutManager      = new LinearLayoutManager(MainActivity.this);
        mRealm              = Realm.getDefaultInstance();

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mLayoutManager);

        getListBook();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();

            }
        });
    }

    public void showDialogAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!mBook.getText().toString().isEmpty() && !mAuthor.getText().toString().isEmpty()){
                    dialog.dismiss();
                    saveToRealm(mBook.getText().toString(),mAuthor.getText().toString());

                }else{
                    Toast.makeText(getApplicationContext(),"Data blm diisi",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Batal",null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_main, null);

        mBook   = (EditText)dialogLayout.findViewById(R.id.book);
        mAuthor = (EditText)dialogLayout.findViewById(R.id.author);

        dialog.setView(dialogLayout);
        dialog.show();

    }

    public void saveToRealm(String book,String author){

        RealmResults<Book> books = mRealm.where(Book.class).findAll();

        mRealm.beginTransaction();

        Book data_book = new Book();

        if(books.size() > 0){
            int id = mRealm.where(Book.class).max("id").intValue();

            data_book.setId(id + 1);
            data_book.setBook(book);
            data_book.setAuthor(author);

            mRealm.copyToRealm(data_book);
        }else{
            data_book.setId(0);
            data_book.setBook(book);
            data_book.setAuthor(author);

            mRealm.copyToRealm(data_book);
        }

        mRealm.commitTransaction();

        mListBook.clear();
        getListBook();

    }

    public void getListBook(){

        RealmResults<Book> listRealms = mRealm.where(Book.class).findAll();

        for (int i = 0; i < listRealms.size(); i++) {
            mListBook.add(listRealms.get(i));
        }

        if(listRealms.size() > 0) {
            mAdapter.setArray(mListBook);
            mRecycler.setAdapter(mAdapter);
            //mEmpty.setVisibility(View.GONE);
        }
    }

    public void UpdateData(final int id, String Book,String Author){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!mBook.getText().toString().isEmpty() && !mAuthor.getText().toString().isEmpty()){
                    dialog.dismiss();
                    updateToRealm(id, mBook.getText().toString(),mAuthor.getText().toString());

                }else{
                    Toast.makeText(getApplicationContext(),"Data blm diisi",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Batal",null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_main,null);

        mBook   = (EditText)dialogLayout.findViewById(R.id.book);
        mAuthor = (EditText)dialogLayout.findViewById(R.id.author);

        mBook.setText(Book);
        mAuthor.setText(Author);

        dialog.setView(dialogLayout);
        dialog.show();

    }

    public void updateToRealm(int id, String Book,String Author ){

        mRealm.beginTransaction();

        Book data_book = new Book();

        data_book.setId(id);
        data_book.setBook(Book);
        data_book.setAuthor(Author);

        mRealm.copyToRealmOrUpdate(data_book);

        mRealm.commitTransaction();

        mListBook.clear();
        getListBook();

        Toast.makeText(getApplicationContext(),"Data Berhasil Diubah",Toast.LENGTH_LONG).show();
    }

    private void setupActionBar() {
        getSupportActionBar().setTitle("List Book");

    }
}
