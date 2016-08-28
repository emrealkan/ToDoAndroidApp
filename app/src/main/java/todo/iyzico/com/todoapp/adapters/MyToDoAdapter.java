package todo.iyzico.com.todoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.models.ToDo;

/**
 * Created by emrealkan on 25/08/16.
 */
public class MyToDoAdapter extends RecyclerView.Adapter<MyToDoAdapter.ViewHolder>{

    private List<ToDo> toDoList;
    private Context context;
    private String[] toDoColors;

    public MyToDoAdapter(Context context, List<ToDo> toDoList)
    {
        this.toDoList = toDoList;
        this.context = context;

        toDoColors = context.getResources().getStringArray(R.array.toDo_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytodo_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ToDo toDo = toDoList.get(position);

        (holder.txt_title).setText(toDo.getTitle());
        (holder.txt_subtitle).setText(toDo.getSubTitle());

        try
        {
            (holder.txt_startDate).setText(toDo.getStartDate());
            (holder.txt_endDate).setText(toDo.getEndDate());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        (holder.txt_content).setText(toDo.getContent());
        int index = Long.valueOf(toDo.getTodoID()).intValue() % 10;
        (holder.toDoLayout).setBackgroundColor(Color.parseColor(toDoColors[index]));
    }

    public List<ToDo> getToDoList()
    {
        return toDoList;
    }

    public int removeToDo(Long todoId)
    {
        for(int i=0; i<toDoList.size(); i++)
        {
            if(toDoList.get(i).getTodoID() == todoId)
            {
                toDoList.remove(i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount()
    {
        return toDoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView txt_title;
        protected TextView txt_subtitle;
        protected TextView txt_startDate;
        protected TextView txt_endDate;
        protected TextView txt_content;
        protected LinearLayout toDoLayout;

        public ViewHolder(View rowView)
        {
            super(rowView);
            txt_title = (TextView) rowView.findViewById(R.id.mytodo_listItem_title);
            txt_subtitle = (TextView) rowView.findViewById(R.id.mytodo_listItem_sub_title);
            txt_startDate = (TextView) rowView.findViewById(R.id.mytodo_listItem_start_date);
            txt_endDate = (TextView) rowView.findViewById(R.id.mytodo_listItem_end_date);
            txt_content = (TextView) rowView.findViewById(R.id.mytodo_listItem_content);
            toDoLayout = (LinearLayout)rowView.findViewById(R.id.toDoLayout);
        }
    }
}
