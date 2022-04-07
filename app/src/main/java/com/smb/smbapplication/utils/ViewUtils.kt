/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
package com.smb.smbapplication.utils

import android.content.res.Resources

object ViewUtils {
    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }
}