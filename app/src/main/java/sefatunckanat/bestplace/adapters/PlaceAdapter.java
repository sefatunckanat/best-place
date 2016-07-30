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
import sefatunckanat.bestplace.screens.Place;
import sefatunckanat.bestplace.screens.ProfileScreen;
import sefatunckanat.bestplace.utils.Variables;

public class PlaceAdapter extends BaseAdapter{

    String [] name,date,url,olay;
    int[] id;
    Context context;
    private static LayoutInflater inflater=null;

    public PlaceAdapter(Place profileScreen, String[] names, int[] ids, String[] dates, String[] urls, String[] os) {
        this.name = names;
        this.id = ids;
        this.date = dates;
        this.url = urls;
        this.olay = os;

        context=profileScreen;
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

    public class Olay
    {
        TextView tvText,tvDate;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Olay holder=new Olay();
        View rowView;
        rowView = inflater.inflate(R.layout.list_notif, null);
        holder.tvText=(TextView) rowView.findViewById(R.id.list_notif_text);
        holder.img=(ImageView) rowView.findViewById(R.id.list_notif_picture);
        holder.tvDate=(TextView)rowView.findViewById(R.id.list_notif_date);

        if(olay[position].contains("enkotu")){
            holder.tvText.setText(name[position]+" mekanı en kötü olarak oyladı.");
        }else if(olay[position].contains("enkalite")){
            holder.tvText.setText(name[position]+" mekanı kaliteli olarak oyladı.");
        }else if(olay[position].contains("enucuz")){
            holder.tvText.setText(name[position]+" mekanı en ucuz olarak oyladı.");
        }else if(olay[position].contains("enpahali")){
            holder.tvText.setText(name[position]+" mekanı en pahalı olarak oyladı.");
        }else if(olay[position].contains("enguzel")){
            holder.tvText.setText(name[position]+" mekanı en güzel olarak oyladı.");
        }else if(olay[position].contains("enrahat")){
            holder.tvText.setText(name[position]+" mekanı en rahat olarak oyladı.");
        }else if(olay[position].contains("ensessiz")){
            holder.tvText.setText(name[position]+" mekanı en sessiz olarak oyladı.");
        }else if(olay[position].contains("enkalabalik")){
            holder.tvText.setText(name[position]+" mekanı en kalablık olarak oyladı.");
        }


        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileScreen.class);
                i.putExtra("ID",id[position]);
                i.putExtra("ME",false);
                context.startActivity(i);
            }
        });

        holder.tvDate.setText(date[position]);
        ImageLoader imgLoader = new ImageLoader(context);
        imgLoader.DisplayImage(Variables.URl_PROFILE_IMAGE+url[position],R.drawable.ic_launcher,holder.img);


        return rowView;
    }

}
