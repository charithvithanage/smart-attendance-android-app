package lnbti.charithgtp01.smartattendanceuserapp.interfaces;

import java.io.File;

/**
 * Sign Pad Dialog Button Click Listener
 */
public interface ImageInspectionListener {
    void onSubmit(File customerSignature);

    void cancel();
}
