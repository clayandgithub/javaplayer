package com.clayoverwind.javaplayer.util;

import com.clayoverwind.javaplayer.view.ChooseDanMuFileDialog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;

public class ClawUtil {
	
	private static HttpClient sHttpClient = new DefaultHttpClient();

	public static String getBilibiliDanmuFileByUrl(final String url) {
		String fileName = null;
        final HttpGet httpGet = new HttpGet(url);
        try {
        	// send request
        	httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        	final HttpResponse response = sHttpClient.execute(httpGet);
            final HttpEntity entity = response.getEntity();

            // unzip to get htmlString
            String htmlString = StringUtil.gzipInputStreamToUTF8String(entity.getContent());

            // get cid from htmlString
            String cid = "";
            {
            	int startIndex = htmlString.indexOf("cid=") + 4;
                int endIndex = htmlString.indexOf("&", startIndex);
                cid = htmlString.substring(startIndex, endIndex);
            }

            //get video name from htmlString
            String videoName = "";
            {
            	int startIndex = htmlString.indexOf("<title>") + 7;
                int endIndex = htmlString.indexOf("</title>", startIndex);
                videoName = StringUtil.removeSpecialChar(htmlString.substring(startIndex, endIndex));
            }

            fileName = ChooseDanMuFileDialog.BILIBILI_DANMU_FILE_PATH_PATTERN.replace("VIDEO_NAME", videoName);
            if (!FileUtil.isFileExist(fileName)) {
            	String danMuUrl = ChooseDanMuFileDialog.BILIBILI_DANMU_URL_PATH_PATTERN.replace("CID", cid);
                HttpUtil.writeRequestEntityIntoFile(ChooseDanMuFileDialog.BILIBILI_DANMU_URL_PATH_PATTERN.replace("CID", cid), fileName);
            }
        } catch (final IOException e) {
        	e.printStackTrace();
            return null;
        } finally {
            httpGet.abort();
        }
        return fileName;
    }
}
