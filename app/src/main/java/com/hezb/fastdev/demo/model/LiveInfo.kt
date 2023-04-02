package com.hezb.fastdev.demo.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name: AndroidFastDev
 * File Name:    LiveInfo
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月26日 22:36
 */
class LiveInfo {

    /** 直播流ID  */
    @SerializedName("push_id")
    var pushId = 0

    /** 直播室ID  */
    @SerializedName("room_id")
    var roomId: String? = null

    /** 直播名字  */
    @SerializedName("title")
    var liveTitle: String? = null

    /** 直播封面图  */
    @SerializedName("logo")
    var logo: String? = null

    /** 游戏ID  */
    @SerializedName("game_id")
    var gameId = 0

    /** 游戏名  */
    @SerializedName("game_name")
    var gameName: String? = null

    /** 游戏图标  */
    @SerializedName("game_logo")
    var gameLogo: String? = null

    /** 播主UID  */
    @SerializedName("uid")
    var uid: String? = null

    /** 播主名字(昵称)  */
    @SerializedName("author")
    var nickName: String? = null

    /** 用户头像  */
    @SerializedName("authorImg")
    var userImg: String? = null

    /** 粉丝数  */
    @SerializedName("fans_num")
    var fansCount = 0

    /** 等级  */
    @SerializedName("level")
    var level = 0

    /** 是否为VIP 0 否 1 是  */
    @SerializedName("author_vip")
    var isVip = 0

    /** 是否为主播 0 否 1 是  */
    @SerializedName("tv_priv")
    var isAuthor = false

    /** 在线人数  */
    @SerializedName("online_nums")
    var onlineCount = 0

    /** 总观看人数  */
    @SerializedName("play_nums")
    var watchCount = 0

    /** 直播地址  */
    @SerializedName("url")
    var liveUrl: String? = null

    /** 直播状态 0 结束 1 直播中  */
    @SerializedName("status")
    var status = 0

    /** 聊天室频道  */
    @SerializedName("channel")
    var channel: String? = null

    /** 统计频道  */
    @SerializedName("tj_channel")
    var tjChannel: String? = null

    /** 直播描述  */
    var liveDescription: String? = null

    /** 聊天室ID  */
    var chatRoomId: String? = null

    /** 是否是新主播 默认否  */
    var isNewAnchor = false

    /** 主播签名  */
    @SerializedName("sign")
    var signature: String? = null

    /** 当前阳光数  */
    @SerializedName("sunshine_value")
    var sunshineNum = 0
    
}