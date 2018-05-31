package xyz.a4tay.dev.hotspot;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class CustomClusterRenderer extends DefaultClusterRenderer<ClusterHandler>
    {
    private final Context mContext;
    Integer theDotAverage;
    Integer theColorCode;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ClusterHandler> clusterManager, Double dotAverage, Integer colorCode)
        {
        super(context, map, clusterManager);
        mContext = context;
        theDotAverage = dotAverage.intValue();
        theColorCode = colorCode;
        }

    @Override protected void
    onBeforeClusterItemRendered(ClusterHandler item, MarkerOptions markerOptions)
        {
        BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.hotspot_marker_5);
        markerOptions.icon(markerDescriptor).snippet("");
        }

    @Override protected void
    onBeforeClusterRendered(Cluster<ClusterHandler> cluster, MarkerOptions markerOptions)
        {
        final IconGenerator mClusterIconGenerator;

        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
        switch (theDotAverage)
            {
            case 1:
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.hotspot1));
                break;
            case 2:
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.hotspot2));
                break;
            case 3:
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.hotspot3));
                break;
            case 4:
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.hotspot4));
                break;
            case 5:
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.hotspot5));
                break;
            default:
                mClusterIconGenerator.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.hotspot5));
                break;
            }

        final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

}
