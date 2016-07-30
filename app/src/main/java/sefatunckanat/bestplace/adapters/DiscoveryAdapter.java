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

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.libImageLoader.ImageLoader;
import sefatunckanat.bestplace.screens.Discovery;
import sefatunckanat.bestplace.screens.NotifsScreen;
import sefatunckanat.bestplace.screens.Place;
import sefatunckanat.bestplace.screens.ProfileScreen;
import sefatunckanat.bestplace.utils.Variables;

public class DiscoveryAdapter extends BaseAdapter{

    String [] name,city,url;
    int[] id;
    Context context;
    private static LayoutInflater inflater=null;

    public DiscoveryAdapter(Discovery activity, String[]name, int[] id, String[] city, String[] url) {
        this.name = name;
        this.id = id;
        this.city = city;
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
        holder.tvText.setText(name[position]);
        holder.tvDate.setText(city[position]);

        ImageLoader imgLoader = new ImageLoader(context);
        imgLoader.DisplayImage(Variables.URl_PROFILE_IMAGE+url[position],R.drawable.ic_launcher,holder.img);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Place.class);
                i.putExtra("ID",id[position]);
                context.startActivity(i);
            }
        });
        return rowView;
    }

}
