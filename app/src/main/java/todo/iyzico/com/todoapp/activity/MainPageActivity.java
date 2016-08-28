package todo.iyzico.com.todoapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.adapters.MyToDoAdapter;
import todo.iyzico.com.todoapp.models.BaseResponse;
import todo.iyzico.com.todoapp.models.ToDo;
import todo.iyzico.com.todoapp.models.User;
import todo.iyzico.com.todoapp.tools.MessageDialog;
import todo.iyzico.com.todoapp.tools.ProgressDialogTool;
import todo.iyzico.com.todoapp.webservice.WebServiceBuilder;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txt_message, nav_header_name, nav_header_email;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshContainer;
    private User user;
    private MyToDoAdapter myTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_message = (TextView) findViewById(R.id.mytodo_txt_message);

        recyclerView = (RecyclerView) findViewById(R.id.mytodo_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainPageActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        user = User.getInstance();
        if(Long.valueOf(user.getId()) == null){
            getUserTodos(user.getId());
        }

        refreshContainer = (SwipeRefreshLayout) findViewById(R.id.mytodo_refreshContainer);

        refreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserTodos(user.getId());
            }
        });

        // Configure the refreshing colors
        refreshContainer.setColorSchemeResources(R.color.ColorPrimary);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
                    refreshContainer.setEnabled(true);
                } else {
                    refreshContainer.setEnabled(false);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new todo.iyzico.com.todoapp.tools.RecyclerItemClickListener(this, recyclerView, new todo.iyzico.com.todoapp.tools.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showMenuDialog(position);
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AddToDoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fiilNavigationViewMenuInformations(user, navigationView);

    }

    private void fiilNavigationViewMenuInformations(User user, NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        nav_header_name = (TextView) headerView.findViewById((R.id.nav_header_name));
        nav_header_email = ((TextView) headerView.findViewById(R.id.nav_header_email));

        if(user.getEmail() != null){
            nav_header_email.setText(user.getEmail());
        }
        if(user.getUserName() != null){
            nav_header_name.setText(user.getUserName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserTodos(User.getInstance().getId());
    }

    private void getUserTodos(final long id) {

        final ProgressDialog progressDialog = ProgressDialogTool.createAndShow(MainPageActivity.this);
        WebServiceBuilder.WebService webService = WebServiceBuilder.getInstance();
        webService.getTodos(id,
                new Callback<BaseResponse<List<ToDo>>>() {
                    @Override
                    public void success(BaseResponse<List<ToDo>> baseResponse, Response response) {
                        ProgressDialogTool.dismiss(progressDialog);
                        refreshContainer.setRefreshing(false);
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()) {
                                List<ToDo> userTodos = baseResponse.getData();
                                if (userTodos.size() > 0) {
                                    MyToDoAdapter adapter = new MyToDoAdapter(getApplicationContext(), userTodos);
                                    recyclerView.setAdapter(adapter);
                                    txt_message.setVisibility(View.INVISIBLE);
                                } else {
                                    txt_message.setVisibility(View.VISIBLE);
                                }
                            } else {
                                MessageDialog.showDialogWithoutActions(MainPageActivity.this, baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogTool.dismiss(progressDialog);
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainPageActivity.this);
                        dlgAlert.setMessage("Connection Error");
                        dlgAlert.setCancelable(true);
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        dlgAlert.setNegativeButton("Try Again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getUserTodos(id);
                                    }
                                });
                        dlgAlert.create().show();
                    }
                }
        );

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
        int id = item.getItemId();
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

        if (id == R.id.nav_settings) {
            // Handle the camera action
        }else if (id == R.id.nav_logout){
            logout();
        }else if (id == R.id.nav_createTodo){
            redirectCreateToDo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        Intent intent = new Intent(MainPageActivity.this, LoginPageActivity.class);
        startActivity(intent);
        MainPageActivity.this.finish();
    }

    private void redirectCreateToDo() {
        Intent intentCreateTodo = new Intent(getApplicationContext(), AddToDoActivity.class);
        startActivity(intentCreateTodo);
    }

    private void showMenuDialog(final int chosenTodoPosition) {
        CharSequence[] items = {"Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyToDoAdapter adapter = (MyToDoAdapter) recyclerView.getAdapter();
                List<ToDo> list = adapter.getToDoList();
                createDialogForWarnUser(list.get(chosenTodoPosition), chosenTodoPosition, false);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDialogForWarnUser(final ToDo swipedAnn, final int position, final boolean addBack) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.delete_todo_warninig_text);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteTodo(swipedAnn.getTodoID());
                dialog.dismiss();
            }
        });
        dlgAlert.setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addBack) {
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

    private void deleteTodo(final Long todoID) {

        final ProgressDialog progressDialog = ProgressDialogTool.createAndShow(MainPageActivity.this);
        WebServiceBuilder.WebService webService = WebServiceBuilder.getInstance();
        webService.deleteTodo(todoID,
                new Callback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse baseResponse, Response response) {
                        ProgressDialogTool.dismiss(progressDialog);
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()) {
                                MyToDoAdapter adapter = (MyToDoAdapter) recyclerView.getAdapter();
                                int position = adapter.removeToDo(todoID);
                                adapter.notifyItemRemoved(position);

                                if(adapter.getToDoList().isEmpty()) {
                                    txt_message.setVisibility(View.VISIBLE);
                                }

                                MessageDialog.showDialogWithoutActions(MainPageActivity.this, "ToDo Deleted succesfully");
                            } else {
                                MessageDialog.showDialogWithoutActions(MainPageActivity.this, baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogTool.dismiss(progressDialog);
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainPageActivity.this);
                        dlgAlert.setMessage("Connection Error");
                        dlgAlert.setCancelable(true);
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        dlgAlert.setNegativeButton("Try Again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteTodo(todoID);
                                    }
                                });
                        dlgAlert.create().show();
                    }
                }
        );

    }


}
