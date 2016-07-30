package sefatunckanat.bestplace.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.libImageLoader.ImageLoader;
import sefatunckanat.bestplace.screens.NotifsScreen;
import sefatunckanat.bestplace.screens.ProfileScreen;
import sefatunckanat.bestplace.utils.Variables;

public class NotifCustomAdapter extends BaseAdapter{

    String [] name,date,url;
    int[] id;
    Context context;
    private static LayoutInflater inflater=null;

    public NotifCustomAdapter(NotifsScreen activity, String[]name,int[] id,String[] date,String[] url) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.url = url;

        context=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Notif
    {
        TextView tvText,tvDate;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Notif holder=new Notif();
        View rowView;
        rowView = inflater.inflate(R.layout.list_notif, null);
        holder.tvText=(TextView) rowView.findViewById(R.id.list_notif_text);
        holder.img=(ImageView) rowView.findViewById(R.id.list_notif_picture);
        holder.tvDate=(TextView)rowView.findViewById(R.id.list_notif_date);
        holder.tvText.setText(name[position]+" sizi takip etmeye başladı.");
        holder.tvDate.setText(date[position]);

        ImageLoader imgLoader = new ImageLoader(context);
        imgLoader.DisplayImage(Variables.URl_PROFILE_IMAGE+url[position],R.drawable.ic_launcher,holder.img);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileScreen.class);
                i.putExtra("ID",id[position]);
                i.putExtra("ME",false);
                context.startActivity(i);
            }
        });
        return rowView;
    }

}
