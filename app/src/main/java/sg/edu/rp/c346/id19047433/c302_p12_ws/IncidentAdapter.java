package sg.edu.rp.c346.id19047433.c302_p12_ws;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IncidentAdapter extends ArrayAdapter<Incident> {
    Context context;
    ArrayList<Incident> incidents;
    int resource;
    TextView tvType,tvMessage;


    public IncidentAdapter(Context context, int resource, ArrayList<Incident> incidents) {
        super(context, resource, incidents);
        this.context = context;
        this.incidents = incidents;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(resource, parent, false);

        tvType = (TextView) rowView.findViewById(R.id.tvType);
        tvMessage = (TextView) rowView.findViewById(R.id.tvMessage);

        Incident incident = incidents.get(position);

        tvType.setText(incident.getType());
        tvMessage.setText(incident.getMessage());

        return rowView;
    }
}

