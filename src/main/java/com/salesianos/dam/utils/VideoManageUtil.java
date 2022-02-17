package com.salesianos.dam.utils;

import com.cloudmersive.client.VideoApi;
import com.cloudmersive.client.invoker.ApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoManageUtil {

    VideoApi apiInstance = new VideoApi();

    Integer maxWidth = 1024; // Integer | Optional; Maximum width of the output video, up to the original video width. Defaults to original video width.

    Integer maxHeight; // Integer | Optional; Maximum height of the output video, up to the original video width. Defaults to original video height.

    Integer frameRate = 30; // Integer | Optional; Specify the frame rate of the output video. Defaults to original video frame rate.

    Integer quality = 56; // Integer | Optional; Specify the quality of the output video, where 100 is lossless and 1 is the lowest possible quality with highest compression. Default is 50.

    public FileOutputStream resizeVideo(String fileLocation,String fileUrl,String extension) throws ApiException, FileNotFoundException {
        File inputFile = new File(fileLocation); // File | Input file to perform the operation on.
        FileOutputStream out = new FileOutputStream(inputFile);
        try {
            byte[] result = apiInstance.videoResizeVideoSimple(inputFile, fileUrl, this.maxWidth, this.maxHeight, this.frameRate, this.quality, extension);
             out.write(result);
             out.close();
            return out;
        } catch (ApiException | IOException e) {
            System.err.println("Exception when calling VideoApi#videoResizeVideoSimple");
            e.printStackTrace();
        }
        return out;
    }
}
