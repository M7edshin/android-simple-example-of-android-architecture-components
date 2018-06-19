package e.m7eds.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This ClientRecyclerAdapter creates and binds ViewHolders, adapter RecyclerView to efficiently display data.
 */
public class ClientRecyclerAdapter extends RecyclerView.Adapter<ClientRecyclerAdapter.ClientViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<Client> clientList;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Constructor for the ClientRecyclerAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public ClientRecyclerAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new ClientViewHolder that holds the view for each task
     */
    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_client_item_list, parent, false);

        return new ClientViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        // Determine the values of the wanted data
        Client client = clientList.get(position);

        String name = client.getName();
        String company = client.getCompany();
        int value = client.getValue();
        String registeredDate = dateFormat.format(client.getRegisteredDate());

        //Set values
        holder.tv_name.setText(name);
        holder.tv_company.setText(company);
        holder.tv_value.setText(String.valueOf(value));
        holder.tv_registered_date.setText(registeredDate);

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (clientList == null) {
            return 0;
        }
        return clientList.size();
    }

    //To get the list as we do not have reference in the main activity when we delete a row
    public List<Client> getClientList(){
        return clientList;
    }

    /**
     * When data changes, this method updates the list of ClientEntries
     * and notifies the adapter to use the new values on it
     */
    public void setClients(List<Client> clients) {
        clientList = clients;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_company;
        TextView tv_value;
        TextView tv_registered_date;

        public ClientViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_company = itemView.findViewById(R.id.tv_company);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_registered_date = itemView.findViewById(R.id.tv_registered_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = clientList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}