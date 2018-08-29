package com.pgy.xmpplib.myglide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.pgy.xmpplib.R;
import com.pgy.xmpplib.manager.XmppManager;
import com.pgy.xmpplib.service.XmVcardConstant;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by burning on 2018/7/9.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * -------------------------//┏┓　　　┏┓
 * -------------------------//┏┛┻━━━┛┻┓
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　━　　　┃
 * -------------------------//┃　┳┛　┗┳　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　┻　　　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┗━┓　　　┏━┛
 * -------------------------//┃　　　┃  神兽保佑
 * -------------------------//┃　　　┃  代码无BUG！
 * -------------------------//┃　　　┗━━━┓
 * -------------------------//┃　　　　　　　┣┓
 * -------------------------//┃　　　　　　　┏┛
 * -------------------------//┗┓┓┏━┳┓┏┛
 * -------------------------// ┃┫┫　┃┫┫
 * -------------------------// ┗┻┛　┗┻┛
 */
public class JmDataFetcher implements DataFetcher<InputStream> {
    String jid;
    private Context context;

    public JmDataFetcher(Context context, String jid) {
        this.jid = jid;
        this.context = context;
//        HttpUrlFetcher
//        LocalUriFetcher
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        callback.onDataReady(getImage());
    }

    private InputStream getImage() {

        VCard user = XmppManager.INSTANCE.getServiceImpl().getUser(context, jid);
        // byte[] userIcon = XmppManager.INSTANCE.getServiceImpl().getUserIcon(context, jid);
        byte[] userIcon = user.getAvatar();
        if (userIcon == null || userIcon.length == 0) {
            // Drawable drawable = context.getDrawable(R.i)
            String field = user.getField(XmVcardConstant.VCARD_GENDER);
            Bitmap bmp = null;
            if (field == null || "0".equals(field)) {
                //女
                bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.woman_logo);
            } else {
                //男
                bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.man_logo2);
            }
            if (bmp == null)
                return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            userIcon = baos.toByteArray();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(
                userIcon);
        return bais;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.DATA_DISK_CACHE;
    }
}
