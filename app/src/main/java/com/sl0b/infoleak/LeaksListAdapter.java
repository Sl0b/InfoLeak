package com.sl0b.infoleak;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.sl0b.infoleak.util.SvgDecoder;
import com.sl0b.infoleak.util.SvgDrawableTranscoder;
import com.sl0b.infoleak.util.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.util.List;

import static com.sl0b.infoleak.util.Utilities.fromHtml;

class LeaksListAdapter extends RecyclerView.Adapter<LeaksListAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Breach> mDataset;
    private int expandedPosition = -1;

    LeaksListAdapter(List<Breach> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    public LeaksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaks_list, parent, false);

        ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(LeaksListAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = mDataset.get(position).getLogoUrl();
        if (mDataset.get(position).isLogoAnSvg()) {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
            requestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .animate(android.R.anim.fade_in)
                    .listener(new SvgSoftwareLayerSetter<Uri>());

            Uri uri = Uri.parse(url);
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(holder.mLogo);
        } else {
            Glide.with(context).load(url).into(holder.mLogo);
        }

        holder.mTitle.setText(mDataset.get(position).getTitle());
        holder.mDomain.setText(mDataset.get(position).getDomain());
        holder.mDate.setText(mDataset.get(position).getBreachDate());
        holder.mDescription.setText(fromHtml(mDataset.get(position).getDescription()));
        holder.mDataClasses.setText(mDataset.get(position).getDataClasses());

        if (position == expandedPosition) {
            holder.expandableZone.setVisibility(View.VISIBLE);
        } else {
            holder.expandableZone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (expandedPosition >= 0) { // Close everything else
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        if (expandedPosition == holder.getAdapterPosition()) { // Close everything
            expandedPosition = -1;
            notifyItemChanged(expandedPosition);
        } else { // Set the current position to "expanded"
            expandedPosition = holder.getAdapterPosition();
            notifyItemChanged(expandedPosition);
        }
    }

    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mLogo;
        final TextView mTitle;
        final TextView mDomain;
        final TextView mDate;
        final TextView mDescription;
        final TextView mDataClasses;
        final CardView mCardView;
        final LinearLayout expandableZone;

        ViewHolder(View view) {
            super(view);

            mLogo = (ImageView) view.findViewById(R.id.logo);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDomain = (TextView) view.findViewById(R.id.domain);
            mDate = (TextView) view.findViewById(R.id.breach_date);
            mDescription = (TextView) view.findViewById(R.id.description);
            mDataClasses = (TextView) view.findViewById(R.id.data_classes);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            expandableZone = (LinearLayout) view.findViewById(R.id.expandable_zone);
        }
    }
}
