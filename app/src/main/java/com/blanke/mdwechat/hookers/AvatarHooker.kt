package com.blanke.mdwechat.hookers

import android.graphics.Bitmap
import com.blanke.mdwechat.Classes.AvatarUtils
import com.blanke.mdwechat.Methods.AvatarUtils_getAvatarBitmaps
import com.blanke.mdwechat.Methods.AvatarUtils_getDefaultAvatarBitmap
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ImageHelper
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object AvatarHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(avatarUtilsHook)
    }

    private val avatarUtilsHook = Hooker {
        XposedHelpers.findAndHookMethod(AvatarUtils, AvatarUtils_getDefaultAvatarBitmap.name, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                if (!HookConfig.is_hook_avatar) {
                    return
                }
                param?.result?.apply {
                    param.result = getCircleBitmap(this as Bitmap)
                }
            }
        })
        AvatarUtils_getAvatarBitmaps.forEach {
            XposedHelpers.findAndHookMethod(AvatarUtils, it.name, C.String, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (!HookConfig.is_hook_avatar) {
                        return
                    }
                    param?.result?.apply {
                        param.result = getCircleBitmap(this as Bitmap)
                    }
                }
            })
        }
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap? {
        return ImageHelper.getRoundedCornerBitmap(bitmap, bitmap.height / 2)
    }
}