package be.ecam.lur.student;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class StudentListActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler{

    private RecyclerView resultView;

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        resultView = (RecyclerView) findViewById(R.id.resultView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resultView.setLayoutManager(layoutManager);
        resultView.setHasFixedSize(true);

        itemAdapter = new ItemAdapter(this);
        resultView.setAdapter(itemAdapter);
    }

    public class QueryTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String searchUrl = params[0];
            String json = null;
            String[] queryResults = null;
            try {
                json = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                Student.parse(json);
                queryResults = Student.getNames();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return queryResults;
        }

        @Override
        protected void onPostExecute(String[] queryResults) {
            if (queryResults != null) {
                itemAdapter.setData(queryResults);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action) {
            //Context context = this;
            //String textToShow = "Action initiated";
            //Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            new QueryTask().execute("http://calendar.ecam.be/list/e");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        Context context = this;
        Class destinationClass = StudentActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        startActivity(intent);
    }
}
