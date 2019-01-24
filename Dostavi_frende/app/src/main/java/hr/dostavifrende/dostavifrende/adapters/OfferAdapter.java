package hr.dostavifrende.dostavifrende.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>{
    private Context mCtx;
    private List<Offer> offerList;

    public OfferAdapter(Context mCtx, List<Offer> offerList) {
        this.mCtx = mCtx;
        this.offerList = offerList;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_active_users, null);
        OfferViewHolder holder = new OfferViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);

        holder.textViewImePrezima.setText(offer.getKorisnik());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class OfferViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewSlika;
        TextView textViewImePrezima, textViewOcjena;

        public OfferViewHolder(View itemView) {
            super(itemView);
            imageViewSlika = itemView.findViewById(R.id.imageViewActiveUser);
            textViewImePrezima = itemView.findViewById(R.id.textViewImePrezime);
            textViewOcjena = itemView.findViewById(R.id.textViewRating);
        }
    }
}
