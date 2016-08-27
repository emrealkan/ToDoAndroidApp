package todo.iyzico.com.todoapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.adapters.MyToDoAdapter;
import todo.iyzico.com.todoapp.models.ToDo;
import todo.iyzico.com.todoapp.models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txt_message;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshContainer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.mytodo_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //TODO dummy data *****************remove and get from server

        txt_message = (TextView) findViewById(R.id.mytodo_txt_message);
        List<ToDo> list = new ArrayList<>();
        ToDo a = new ToDo();
        ToDo b = new ToDo();
        a.setTitle("First Title");
        b.setTitle("Second Title");
        a.setSubTitle("First SubTitle");
        b.setSubTitle("Second SubTitle");
        a.setContent("First Content");
        b.setContent("Second Content");
        a.setEndDate("First EndDate");
        b.setEndDate("Second EndDate");
        a.setStartDate("First StartDate");
        b.setStartDate("Second StarDate");

        list.add(0,a);
        list.add(0,b);

        MyToDoAdapter toDoAdapter = new MyToDoAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(toDoAdapter);
        txt_message.setVisibility(View.INVISIBLE);
        //****************************

        refreshContainer = (SwipeRefreshLayout) findViewById(R.id.mytodo_refreshContainer);

        refreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //new LongRunningGetIO().execute();
            }
        });
        // Configure the refreshing colors
        refreshContainer.setColorSchemeResources(R.color.ColorPrimary);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)
                {
                    refreshContainer.setEnabled(true);
                }
                else
                {
                    refreshContainer.setEnabled(false);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new todo.iyzico.com.todoapp.tools.RecyclerItemClickListener(this, recyclerView, new todo.iyzico.com.todoapp.tools.RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                /*MyAnnouncementsListAdapter adapter = (MyAnnouncementsListAdapter) recyclerView.getAdapter();
                List<Announcement> list = adapter.getAnnouncementList();*/
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                showMenuDialog(position);
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddToDoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMenuDialog(final int chosenConversationPosition)
    {
        CharSequence[] items = {"Sil"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                MyToDoAdapter adapter = (MyToDoAdapter) recyclerView.getAdapter();
                List<ToDo> list = adapter.getToDoList();
                createDialogForWarnUser(list.get(chosenConversationPosition), chosenConversationPosition, false);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDialogForWarnUser(final ToDo swipedAnn, final int position, final boolean addBack)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.delete_todo_warninig_text);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                //Delete Todo
                //new DeleteToDoTask(swipedAnn.getAnnID(), !addBack).execute();
                dialog.dismiss();
            }
        });
        dlgAlert.setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(addBack)
                {
                    MyToDoAdapter adapter = (MyToDoAdapter) recyclerView.getAdapter();
                    List<ToDo> list = adapter.getToDoList();
                    list.add(position, swipedAnn);
                    adapter.notifyItemInserted(position);
                }
                dialog.dismiss();
            }
        });
        dlgAlert.create().show();
    }


}
