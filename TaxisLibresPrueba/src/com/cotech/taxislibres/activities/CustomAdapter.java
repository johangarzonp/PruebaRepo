package com.cotech.taxislibres.activities;

import java.util.List;




import com.cotech.taxislibres.C;
import com.cotech.taxislibres.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
	Context context;
	List<RowItem> rowItems;
	String RtaDelete;

	public CustomAdapter(Context context, List<RowItem> rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView profile_pic;
		TextView member_name;
		TextView status;
		Button delete;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();

			holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
			holder.profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			


			RowItem row_pos = rowItems.get(position);

			holder.profile_pic.setImageResource(row_pos.getProfile_pic_id());
			holder.member_name.setText(row_pos.getMember_name());
			holder.status.setText(row_pos.getStatus());

			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//Toast.makeText(context, "Delete " + rowItems.get(position).getIdTarjeta() , Toast.LENGTH_LONG).show();
					Intent respuestaBorrado = new Intent();
					respuestaBorrado.putExtra("DATA",rowItems.get(position).getIdTarjeta());
					respuestaBorrado.putExtra("CMD", C.BORRAR_TARJETA);
					respuestaBorrado.setAction(C.LISTAR_TARJETAS);
					context.sendBroadcast(respuestaBorrado);

				}
			});


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}
}
