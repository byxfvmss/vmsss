var JSON = function () {
    var m = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        },
        s = {
            'boolean': function (x) {
                return String(x);
            },
            number: function (x) {
                return isFinite(x) ? String(x) : 'null';
            },
            string: function (x) {
                if (/["\\\x00-\x1f]/.test(x)) {
                    x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
                        var c = m[b];
                        if (c) {
                            return c;
                        }
                        c = b.charCodeAt();
                        return '\\u00' +
                            Math.floor(c / 16).toString(16) +
                            (c % 16).toString(16);
                    });
                }
                return '"' + x + '"';
            },
            object: function (x) {
                if (x) {
                    var a = [], b, f, i, l, v;
                    if (x instanceof Array) {
                        a[0] = '[';
                        l = x.length;
                        for (i = 0; i < l; i += 1) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a[a.length] = v;
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = ']';
                    } else if (x instanceof Object) {
                        a[0] = '{';
                        for (i in x) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a.push(s.string(i), ':', v);
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = '}';
                    } else {
                        return;
                    }
                    return a.join('');
                }
                return 'null';
            }
        };
    return {
        copyright: '(c)2005 JSON.org',
        license: 'http://www.crockford.com/JSON/license.html',
/*
    Stringify a JavaScript value, producing a JSON text.
*/
        stringify: function (v) {
            var f = s[typeof v];
            if (f) {
                v = f(v);
                if (typeof v == 'string') {
                    return v;
                }
            }
            return null;
        },
/*
    Parse a JSON text, producing a JavaScript value.
    It returns false if there is a syntax error.
*/
        parse: function (text) {
            try {
                return !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
                        text.replace(/"(\\.|[^"\\])*"/g, ''))) &&
                    eval('(' + text + ')');
            } catch (e) {
                return false;
            }
        }
    };
}();
/**
 * JavaScript Common Templates(jCT) 3(绗�鐗�
 * http://code.google.com/p/jsct/
 *
 * licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 *
 * Author achun (achun.shx at gmail.com)
 * Create Date: 2008-6-23
 * Last Date: 2008-10-29
 * Revision:3.8.10.29
 */
function jCT(txt,path){//鏋勫缓jCT瀵硅薄,浠呬粎鍑嗗鍩虹鏁版嵁
	this.Fn = new arguments.callee.Instance(txt,path);
	for (var i in this)
		this.Fn.Reserve+=','+i+',';
}
jCT.prototype={
	Extend:function(jct){//鎵╁睍鑷繁
		for (var i in jct){
			if(this[i] && this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && this[i].Extend )
				this[i].Extend(jct[i]);
			else if(this.Fn.Reserve.indexOf(','+i+',')==-1)//闃叉淇濈暀鎴愬憳琚鐩�
				this[i]=jct[i];
		}
		if(typeof jct.RunNow=='function')
			jct.RunNow.call(this);
		return this;
	},
	ExtendTo:function(jct){//闄勫姞鍒板叾浠栧璞′笂
		for (var i in this){
			if(this.Fn.Reserve.indexOf(','+i+',')>0 && jct[i]) continue;
			if(jct[i]==null)
				jct[i]=this[i];
			else if(this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && this[i].ExtendTo)
				this[i].ExtendTo(jct[i]);
		}
		if(typeof jct.RunNow=='function')
			jct.RunNow();
		return this;
	},
	ExecChilds:function(childs,exec){//鎵цchilds瀵硅薄鎵�垪鎴愬憳閲岀殑鏌愪釜鏂规硶锛岄粯璁ゆ槸Exec鏂规硶
		if(typeof childs=='string'){
			exec=childs;
			childs=0;
		}else
			exec=exec||'Exec';
		if(!childs){
			childs={};
			for (var c in this)
				if(this[c].Fn && this[c].Fn.JavaScriptCommonTemplates)
					childs[c]=false;
		}
		for(var c in childs)
			if(this[c] && (typeof this[c][exec]=='function')){
				this[c][exec](childs[c]);
		}
		return this;
	},
	BuildChilds:function(childs){//鏋勫缓瀛恓CT瀵硅薄,骞舵墽琛孯unNow
		if(undefined==childs) childs=[];
		if (typeof childs=='string') childs=childs.split(',');
		var cs={};
		for(var i=0;i<childs.length;i++) cs[childs[i]]=true;
		for (var i in this)
		if(this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && (childs.length==0 || cs[i]))
			this[i].Build();
		return this;
	},
	GetView:function(){return 'Invalid templates';},//寰楀埌瑁呴厤鏁版嵁鍚庣殑瑙嗗浘锛屾鏂规硶浼氬湪Build鐨勮繃绋嬩腑閲嶅缓,骞朵笖娓呴櫎杈撳嚭缂撳瓨
	GetViewContinue:function(){return 'Invalid templates';},//寰楀埌瑁呴厤鏁版嵁鍚庣殑瑙嗗浘锛屾鏂规硶浼氬湪Build鐨勮繃绋嬩腑閲嶅缓
	Build:function(txt,path){//鏋勫缓瀹炰緥
		this.Fn.Init(txt,path);
		this.Fn.Build(this);
		return this;
	}
};
jCT.Instance=function(txt,path){
	this.Src=txt||'';
	this.Path=path||'';
};
jCT.Instance.prototype={
	JavaScriptCommonTemplates:3.0,
	Reserve:'',//淇濈暀鎴愬憳
	Tags:{//鍑犵涓嶅悓鐨勬ā鏉垮畾涔夐鏍�
		comment:{//娉ㄩ噴鏍囩椋庢牸
			block:{begin:'<!---',end:'-->'},//璇硶鍧楁爣璁�
			exp:{begin:'+-',end:'-+'},//鍙栧�琛ㄨ揪寮�
			member:{begin:'/*+',end:'*/'},//瀹氫箟鎴愬憳璇硶鏍囪
			memberend:{begin:'/*-',end:'*/'},//瀹氫箟鎴愬憳缁撴潫璇硶鏍囪
			clean:{begin:'<!--clean',end:'/clean-->'}//娓呯悊鏍囪
		},
		script:{//鑴氭湰鏍囩椋庢牸
			block:{begin:'<script type="text/jct">',end:'</script>'},
			exp:{begin:'+-',end:'-+'},
			member:{begin:'/*+',end:'*/'},
			memberend:{begin:'/*-',end:'*/'},
			clean:{begin:'<!--clean',end:'/clean-->'}
		},
		code:{//code鏍囩椋庢牸
			block:{begin:'<code class="jct">',end:'</code>'},
			exp:{begin:'+-',end:'-+'},
			member:{begin:'/*+',end:'*/'},
			memberend:{begin:'/*-',end:'*/'},
			clean:{begin:'<!--clean',end:'/clean-->'}
		}
	},
	Init:function(txt,path){
		if(txt!=undefined) this.Src=txt;
		if(path!=undefined) this.Path=path;
		for (var tag in this.Tags)//鑷姩鍒ゆ柇妯℃澘椋庢牸
			if (this.Src.indexOf(this.Tags[tag].block.begin)>=0) break;
		this.Tag=this.Tags[tag];
		this.A=[];//鐢眘rc杞崲鐨勬ā鏉挎暟缁�
		this.V=[];//鎵ц鐨勬枃鏈粨鏋�浠ユ暟缁勫舰寮忓瓨鏀�
		this.EXEC=[];//
		var a=[];
		var p=[0,0,0,0,0];
		var max=this.Src.length;
		while (this.Slice(this.Tag.clean,p[4],p,max))
			a.push(this.Src.slice(p[0],p[1]));
		if(a.length){
			a.push(this.Src.slice(p[4]));
			this.Src = a.join('');
		}
	},
	Build:function(self){
		this.EXEC=[];
		this.Parse(self);
		try{
			var code=this.EXEC.join('\n');
			self.GetViewContinue=new Function(code);
			self.GetView=function(){this.Fn.V=[];this.GetViewContinue.apply(this,arguments);return this.Fn.V.join('');};
		}catch (ex){
			this.V=['jCT Parse Error'];
			self.ERROR={message:ex.message + '\n'+ (ex.lineNumber || ex.number),code:code};
		}
		if(self.RunNow)
			self.RunNow();
	},
	Parse:function(self){
		var tag = this.Tag,A = this.A,E=this.EXEC,max= this.Src.length,p=[0,0,0,0,0],p1=[0,0,0,0,0];
		while (this.Slice(tag.block,p[4],p,max)){//璇硶鍒�娈�
			p1=[0,0,0,0,p[0]];
			while (this.Slice(tag.exp,p1[4],p1,p[1])){//澶勭悊鍙栧�琛ㄨ揪寮�
				E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
				A.push(this.Src.slice(p1[0],p1[1]));
				E.push('this.Fn.V.push('+this.Src.slice(p1[2],p1[3])+');');
			}
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[4],p[1]));
			if (this.Slice(tag.member,p[2],p1,p[3])){//澶勭悊鎵╁睍璇硶
				var str=this.Src.slice(p1[2],p1[3]);
				var foo=this.Src.slice(p1[4],p[3]);
				if (str.slice(0,1)=='@'){//瀛愭ā鏉�
					var child=tag.block.begin+tag.memberend.begin+str+tag.memberend.end+tag.block.end;
					var tmp = this.Src.indexOf(child,p[4]);
					if (tmp>0){
						var njct=new jCT(this.Src.slice(p[4],tmp),this.Path);
						if(!self[str.slice(1)]) self[str.slice(1)]={};
						for (var j in njct) 
							self[str.slice(1)][j]=njct[j];
						p[4] = tmp + child.length;
					}
				}else if (str.slice(0,1)=='$'){//鎴愬憳瀵硅薄
					var obj=new Function('return '+foo+';');
					self[str.slice(1)]=obj.call(self);
				}else //鎴愬憳鍑芥暟
					self[str]=new Function(foo);
			}else//javascript璇彞
				E.push(this.Src.slice(p[2],p[3]));
		}
		p1=[0,0,0,0,p[4]];p[1]=max;
		while (this.Slice(tag.exp,p1[4],p1,p[1])){//澶勭悊鍙栧�琛ㄨ揪寮�
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[0],p1[1]));
			E.push('this.Fn.V.push('+this.Src.slice(p1[2],p1[3])+');');
		}
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[4],p[1]));
	},
	Slice:function(tn,b1,p,max){//鎶妔tring绗�娈靛垎鎴�娈�
		var begin=tn.begin;
		var end=tn.end;
		var e1,b2,e2;
		e1=this.Src.indexOf(begin,b1);
		if (e1<0 || e1>=max) return false;
		b2=e1+begin.length;
		if (b2<0 || b2>=max) return false;
		e2=this.Src.indexOf(end,b2);
		if (e2<0 || e2>=max) return false;
		p[0]=b1;p[1]=e1;
		p[2]=b2;p[3]=e2;
		p[4]=e2+end.length;
		return true;
	}
};

var ua=navigator.userAgent.toLowerCase();
var isOpera=(ua.indexOf('opera')>-1);
var isSafari=(ua.indexOf('safari')>-1);
var isIE=(!isOpera&&ua.indexOf('msie')>-1);
var isNs=ua.indexOf('mozilla')>-1;

//甯搁噺瀹氫箟锛岃鍕跨洿鎺ヤ娇鐢ㄥ�!!
var ORDER_ASC = 'asc';
var ORDER_DESC = 'desc';
var TYPE_LUNAR = 'lunar';


var ieBody = (document.compatMode && document.compatMode != "BackCompat") ? document.documentElement : document.body;


var bind = function(func, obj){
	var __method = func;
	return function(){
		var args = [];
		for (var i = 0; i < arguments.length; i++) 
			args[i] = arguments[i];
		return __method.apply(obj, args);
	}
};

/* jQuery DOM */
jQuery.dom = function(elementId){
	return document.getElementById(elementId);
};
jQuery.fn.extend({
	innerHTML: function(html){
		if (arguments.length == 0) {
			return this[0].innerHTML;
		}
		this[0].innerHTML = html;
		return this;
	}
});
/**
* hoverIntent r5 // 2007.03.27 // jQuery 1.1.2+
* <http://cherne.net/brian/resources/jquery.hoverIntent.html>
* 
* @param  f  onMouseOver function || An object with configuration options
* @param  g  onMouseOut function  || Nothing (use configuration options object)
* @author    Brian Cherne 
*/
(function($){$.fn.hoverIntent=function(f,g){var cfg={sensitivity:7,interval:100,timeout:0};cfg=$.extend(cfg,g?{over:f,out:g}:f);var cX,cY,pX,pY;var track=function(ev){cX=ev.pageX;cY=ev.pageY;};var compare=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);if((Math.abs(pX-cX)+Math.abs(pY-cY))<cfg.sensitivity){$(ob).unbind("mousemove",track);ob.hoverIntent_s=1;return cfg.over.apply(ob,[ev]);}else{pX=cX;pY=cY;ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}};var delay=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);ob.hoverIntent_s=0;return cfg.out.apply(ob,[ev]);};var handleHover=function(e){var p=(e.type=="mouseover"?e.fromElement:e.toElement)||e.relatedTarget;while(p&&p!=this){try{p=p.parentNode;}catch(e){p=this;}}if(p==this){return false;}var ev=jQuery.extend({},e);var ob=this;if(ob.hoverIntent_t){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);}if(e.type=="mouseover"){pX=ev.pageX;pY=ev.pageY;$(ob).bind("mousemove",track);if(ob.hoverIntent_s!=1){ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}}else{$(ob).unbind("mousemove",track);if(ob.hoverIntent_s==1){ob.hoverIntent_t=setTimeout(function(){delay(ev,ob);},cfg.timeout);}}};return this.mouseover(handleHover).mouseout(handleHover);};})(jQuery);
/* jQuery cookie plugin start */
/* jQuery cookie plugin end */
function StringBuffer(){
	this._strings = new Array();
}

StringBuffer.prototype.append = function(str){
	this._strings.push(str);
	return this;
};
StringBuffer.prototype.toString = function(){
	var str = arguments.length == 0 ? '' : arguments[0];
	return this._strings.join(str);
};
String.prototype.leftpad = function(len, str){
	if (!str) {
		str = '0';
	}
	
	var s = '';
	for (var i = 0; i < len - this.length; i++) {
		s += str;
	}
	return s + this;
}
if (!window.rili) {
	window.rili = new Object();
}
if (!window.rili.controls) {
	window.rili.controls = new Object();
}
if (!window.rili.util) {
	window.rili.util = new Object();
}
var $breakEvent=new Object();
//===============================================huangli.hs===================//
var HuangLi = {};
HuangLi.y2010=JSON.parse('{}');
//===============================================calendarObj.js========================//
/*****************************************************************************
                                   鏃ユ湡璧勬枡
*****************************************************************************/

var lunarInfo=new Array(
0x4bd8,0x4ae0,0xa570,0x54d5,0xd260,0xd950,0x5554,0x56af,0x9ad0,0x55d2,
0x4ae0,0xa5b6,0xa4d0,0xd250,0xd295,0xb54f,0xd6a0,0xada2,0x95b0,0x4977,
0x497f,0xa4b0,0xb4b5,0x6a50,0x6d40,0xab54,0x2b6f,0x9570,0x52f2,0x4970,
0x6566,0xd4a0,0xea50,0x6a95,0x5adf,0x2b60,0x86e3,0x92ef,0xc8d7,0xc95f,
0xd4a0,0xd8a6,0xb55f,0x56a0,0xa5b4,0x25df,0x92d0,0xd2b2,0xa950,0xb557,
0x6ca0,0xb550,0x5355,0x4daf,0xa5b0,0x4573,0x52bf,0xa9a8,0xe950,0x6aa0,
0xaea6,0xab50,0x4b60,0xaae4,0xa570,0x5260,0xf263,0xd950,0x5b57,0x56a0,
0x96d0,0x4dd5,0x4ad0,0xa4d0,0xd4d4,0xd250,0xd558,0xb540,0xb6a0,0x95a6,
0x95bf,0x49b0,0xa974,0xa4b0,0xb27a,0x6a50,0x6d40,0xaf46,0xab60,0x9570,
0x4af5,0x4970,0x64b0,0x74a3,0xea50,0x6b58,0x5ac0,0xab60,0x96d5,0x92e0,
0xc960,0xd954,0xd4a0,0xda50,0x7552,0x56a0,0xabb7,0x25d0,0x92d0,0xcab5,
0xa950,0xb4a0,0xbaa4,0xad50,0x55d9,0x4ba0,0xa5b0,0x5176,0x52bf,0xa930,
0x7954,0x6aa0,0xad50,0x5b52,0x4b60,0xa6e6,0xa4e0,0xd260,0xea65,0xd530,
0x5aa0,0x76a3,0x96d0,0x4afb,0x4ad0,0xa4d0,0xd0b6,0xd25f,0xd520,0xdd45,
0xb5a0,0x56d0,0x55b2,0x49b0,0xa577,0xa4b0,0xaa50,0xb255,0x6d2f,0xada0,
0x4b63,0x937f,0x49f8,0x4970,0x64b0,0x68a6,0xea5f,0x6b20,0xa6c4,0xaaef,
0x92e0,0xd2e3,0xc960,0xd557,0xd4a0,0xda50,0x5d55,0x56a0,0xa6d0,0x55d4,
0x52d0,0xa9b8,0xa950,0xb4a0,0xb6a6,0xad50,0x55a0,0xaba4,0xa5b0,0x52b0,
0xb273,0x6930,0x7337,0x6aa0,0xad50,0x4b55,0x4b6f,0xa570,0x54e4,0xd260,
0xe968,0xd520,0xdaa0,0x6aa6,0x56df,0x4ae0,0xa9d4,0xa4d0,0xd150,0xf252,
0xd520);

var solarMonth=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
var Gan=new Array("鐢�,"涔�,"涓�,"涓�,"鎴�,"宸�,"搴�,"杈�,"澹�,"鐧�);
var Zhi=new Array("瀛�,"涓�,"瀵�,"鍗�,"杈�,"宸�,"鍗�,"鏈�,"鐢�,"閰�,"鎴�,"浜�);
var Animals=new Array("榧�,"鐗�,"铏�,"鍏�,"榫�,"铔�,"椹�,"缇�,"鐚�,"楦�,"鐙�,"鐚�);
var solarTerm = new Array("灏忓瘨","澶у瘨","绔嬫槬","闆ㄦ按","鎯婅洶","鏄ュ垎","娓呮槑","璋烽洦","绔嬪","灏忔弧","鑺掔","澶忚嚦","灏忔殤","澶ф殤","绔嬬","澶勬殤","鐧介湶","绉嬪垎","瀵掗湶","闇滈檷","绔嬪啲","灏忛洩","澶ч洩","鍐嚦");
var sTermInfo = new Array(0,21208,42467,63836,85337,107014,128867,150921,173149,195551,218072,240693,263343,285989,308563,331033,353350,375494,397447,419210,440795,462224,483532,504758);
var nStr1 = new Array('鏃�,'涓�,'浜�,'涓�,'鍥�,'浜�,'鍏�,'涓�,'鍏�,'涔�,'鍗�);
var nStr2 = new Array('鍒�,'鍗�,'寤�,'鍗�,'鈻�);
var monthName = new Array("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
var cmonthName = new Array('姝�,'浜�,'涓�,'鍥�,'浜�,'鍏�,'涓�,'鍏�,'涔�,'鍗�,'鍗佷竴','鑵�);

//鍏巻鑺傛棩 *琛ㄧず鏀惧亣鏃�
var sFtv = new Array(
"0101*鏂板勾鍏冩棪",
"0202 涓栫晫婀垮湴鏃�,
"0207 鍥介檯澹版彺鍗楅潪鏃�,
"0210 鍥介檯姘旇薄鑺�,
"0214 鎯呬汉鑺�,
"0301 鍥介檯娴疯惫鏃�,
"0303 鍏ㄥ浗鐖辫�鏃�,
"0308 鍥介檯濡囧コ鑺�,
"0312 妞嶆爲鑺�瀛欎腑灞遍�涓栫邯蹇垫棩",
"0314 鍥介檯璀﹀療鏃�,
"0315 鍥介檯娑堣垂鑰呮潈鐩婃棩",
"0317 涓浗鍥藉尰鑺�鍥介檯鑸捣鏃�,
"0321 涓栫晫妫灄鏃�娑堥櫎绉嶆棌姝ц鍥介檯鏃�,
"0321 涓栫晫鍎挎瓕鏃�,
"0322 涓栫晫姘存棩",
"0323 涓栫晫姘旇薄鏃�,
"0324 涓栫晫闃叉不缁撴牳鐥呮棩",
"0325 鍏ㄥ浗涓皬瀛︾敓瀹夊叏鏁欒偛鏃�,
"0330 宸村嫆鏂潶鍥藉湡鏃�,
"0401 鎰氫汉鑺�鍏ㄥ浗鐖卞浗鍗敓杩愬姩鏈�鍥涙湀) 绋庢敹瀹ｄ紶鏈�鍥涙湀)",
"0407 涓栫晫鍗敓鏃�,
"0422 涓栫晫鍦扮悆鏃�,
"0423 涓栫晫鍥句功鍜岀増鏉冩棩",
"0424 浜氶潪鏂伴椈宸ヤ綔鑰呮棩",
"0501 鍥介檯鍔冲姩鑺�,
"0504 涓浗浜斿洓闈掑勾鑺�,
"0505 纰樼己涔忕梾闃叉不鏃�,
"0508 涓栫晫绾㈠崄瀛楁棩",
"0512 鍥介檯鎶ゅ＋鑺�,
"0515 鍥介檯瀹跺涵鏃�,
"0517 涓栫晫鐢典俊鏃�,
"0518 鍥介檯鍗氱墿棣嗘棩",
"0520 鍏ㄥ浗瀛︾敓钀ュ吇鏃�,
"0523 鍥介檯鐗涘ザ鏃�,
"0531 涓栫晫鏃犵儫鏃�, 
"0601 鍥介檯鍎跨鑺�,
"0605 涓栫晫鐜鏃�,
"0606 鍏ㄥ浗鐖辩溂鏃�,
"0617 闃叉不鑽掓紶鍖栧拰骞叉棻鏃�,
"0623 鍥介檯濂ユ灄鍖瑰厠鏃�,
"0625 鍏ㄥ浗鍦熷湴鏃�,
"0626 鍥介檯鍙嶆瘨鍝佹棩",
"0701 涓浗鍏变骇鍏氬缓鍏氭棩 涓栫晫寤虹瓚鏃�,
"0702 鍥介檯浣撹偛璁拌�鏃�,
"0707 涓浗浜烘皯鎶楁棩鎴樹簤绾康鏃�,
"0711 涓栫晫浜哄彛鏃�,
"0730 闈炴床濡囧コ鏃�,
"0801 涓浗寤哄啗鑺�,
"0808 涓浗鐢峰瓙鑺�鐖哥埜鑺�",
"0815 鏃ユ湰姝ｅ紡瀹ｅ竷鏃犳潯浠舵姇闄嶆棩",
"0908 鍥介檯鎵洸鏃�鍥介檯鏂伴椈宸ヤ綔鑰呮棩",
"0910 鏁欏笀鑺�,
"0914 涓栫晫娓呮磥鍦扮悆鏃�,
"0916 鍥介檯鑷哀灞備繚鎶ゆ棩",
"0918 涔澛蜂竴鍏簨鍙樼邯蹇垫棩",
"0920 鍥介檯鐖辩墮鏃�,
"0927 涓栫晫鏃呮父鏃�,
"1001*鍥藉簡鑺�涓栫晫闊充箰鏃�鍥介檯鑰佷汉鑺�,
"1001 鍥介檯闊充箰鏃�,
"1002 鍥介檯鍜屽钩涓庢皯涓昏嚜鐢辨枟浜夋棩",
"1004 涓栫晫鍔ㄧ墿鏃�,
"1008 鍏ㄥ浗楂樿鍘嬫棩",
"1008 涓栫晫瑙嗚鏃�,
"1009 涓栫晫閭斂鏃�涓囧浗閭仈鏃�,
"1010 杈涗亥闈╁懡绾康鏃�涓栫晫绮剧鍗敓鏃�,
"1013 涓栫晫淇濆仴鏃�鍥介檯鏁欏笀鑺�,
"1014 涓栫晫鏍囧噯鏃�,
"1015 鍥介檯鐩蹭汉鑺�鐧芥墜鏉栬妭)",
"1016 涓栫晫绮鏃�,
"1017 涓栫晫娑堥櫎璐洶鏃�,
"1022 涓栫晫浼犵粺鍖昏嵂鏃�,
"1024 鑱斿悎鍥芥棩 涓栫晫鍙戝睍淇℃伅鏃�,
"1031 涓栫晫鍕や凯鏃�,
"1107 鍗佹湀绀句細涓讳箟闈╁懡绾康鏃�,
"1108 涓浗璁拌�鏃�,
"1109 鍏ㄥ浗娑堥槻瀹夊叏瀹ｄ紶鏁欒偛鏃�,
"1110 涓栫晫闈掑勾鑺�,
"1111 鍥介檯绉戝涓庡拰骞冲懆(鏈棩鎵�睘鐨勪竴鍛�",
"1112 瀛欎腑灞辫癁杈扮邯蹇垫棩",
"1114 涓栫晫绯栧翱鐥呮棩",
"1117 鍥介檯澶у鐢熻妭 涓栫晫瀛︾敓鑺�,
"1121 涓栫晫闂�鏃�涓栫晫鐢佃鏃�,
"1129 鍥介檯澹版彺宸村嫆鏂潶浜烘皯鍥介檯鏃�,
"1201 涓栫晫鑹炬粙鐥呮棩",
"1203 涓栫晫娈嬬柧浜烘棩",
"1205 鍥介檯缁忔祹鍜岀ぞ浼氬彂灞曞織鎰夸汉鍛樻棩",
"1208 鍥介檯鍎跨鐢佃鏃�,
"1209 涓栫晫瓒崇悆鏃�,
"1210 涓栫晫浜烘潈鏃�,
"1212 瑗垮畨浜嬪彉绾康鏃�,
"1213 鍗椾含澶у睜鏉�1937骞�绾康鏃ワ紒绱ц琛�唱鍙诧紒",
"1221 鍥介檯绡悆鏃�,
"1224 骞冲畨澶�,
"1225 鍦ｈ癁鑺�,
"1229 鍥介檯鐢熺墿澶氭牱鎬ф棩");

//鏌愭湀鐨勭鍑犱釜鏄熸湡鍑犮� 5,6,7,8 琛ㄧず鍒版暟绗�1,2,3,4 涓槦鏈熷嚑
var wFtv = new Array(
"0110 榛戜汉鏃�,
"0150 涓栫晫楹婚鏃�, //涓�湀鐨勬渶鍚庝竴涓槦鏈熸棩锛堟湀鍊掓暟绗竴涓槦鏈熸棩锛�
"0520 鍥介檯姣嶄翰鑺�,
"0530 鍏ㄥ浗鍔╂畫鏃�,
"0630 鐖朵翰鑺�,
"0932 鍥介檯鍜屽钩鏃�,
"0940 鍥介檯鑱嬩汉鑺�涓栫晫鍎跨鏃�,
"0950 涓栫晫娴蜂簨鏃�,
"1011 鍥介檯浣忔埧鏃�,
"1013 鍥介檯鍑忚交鑷劧鐏惧鏃�鍑忕伨鏃�",
"1144 鎰熸仼鑺�);

//鍐滃巻鑺傛棩
var lFtv = new Array(
"0101*鏄ヨ妭",
"0115 鍏冨鑺�,
"0202 榫欐姮澶磋妭",
"0323 濡堢鐢熻景 (澶╀笂鍦ｆ瘝璇炶景)",
"0505 绔崍鑺�,
"0707 涓冧竷涓浗鎯呬汉鑺�,
"0815 涓鑺�,
"0909 閲嶉槼鑺�,
"1208 鑵婂叓鑺�,
"1223 灏忓勾",
"0100*闄ゅ");


/*****************************************************************************
                                      鏃ユ湡璁＄畻
*****************************************************************************/

//====================================== 杩斿洖鍐滃巻 y骞寸殑鎬诲ぉ鏁�
function lYearDays(y) {
 var i, sum = 348;
 for(i=0x8000; i>0x8; i>>=1) sum += (lunarInfo[y-1900] & i)? 1: 0;
 return(sum+leapDays(y));
}

//====================================== 杩斿洖鍐滃巻 y骞撮棸鏈堢殑澶╂暟
function leapDays(y) {
 if(leapMonth(y)) return( (lunarInfo[y-1899]&0xf)==0xf? 30: 29);
 else return(0);
}

//====================================== 杩斿洖鍐滃巻 y骞撮棸鍝釜鏈�1-12 , 娌￠棸杩斿洖 0
function leapMonth(y) {
 var lm = lunarInfo[y-1900] & 0xf;
 return(lm==0xf?0:lm);
}

//====================================== 杩斿洖鍐滃巻 y骞磎鏈堢殑鎬诲ぉ鏁�
function monthDays(y,m) {
 return( (lunarInfo[y-1900] & (0x10000>>m))? 30: 29 );
}



//====================================== 绠楀嚭鍐滃巻, 浼犲叆鏃ユ湡鎺т欢, 杩斿洖鍐滃巻鏃ユ湡鎺т欢
//                                       璇ユ帶浠跺睘鎬ф湁 .year .month .day .isLeap
function Lunar(objDate) {

   var i, leap=0, temp=0;
   var offset   = (Date.UTC(objDate.getFullYear(),objDate.getMonth(),objDate.getDate()) - Date.UTC(1900,0,31))/86400000;

   for(i=1900; i<2100 && offset>0; i++) { temp=lYearDays(i); offset-=temp; }

   if(offset<0) { offset+=temp; i--; }

   this.year = i;

   leap = leapMonth(i); //闂板摢涓湀
   this.isLeap = false;

   for(i=1; i<13 && offset>0; i++) {
      //闂版湀
      if(leap>0 && i==(leap+1) && this.isLeap==false)
         { --i; this.isLeap = true; temp = leapDays(this.year); }
      else
         { temp = monthDays(this.year, i); }

      //瑙ｉ櫎闂版湀
      if(this.isLeap==true && i==(leap+1)) this.isLeap = false;

      offset -= temp;
   }

   if(offset==0 && leap>0 && i==leap+1)
      if(this.isLeap)
         { this.isLeap = false; }
      else
         { this.isLeap = true; --i; }

   if(offset<0){ offset += temp; --i; }

   this.month = i;
   this.day = offset + 1;
}

function getSolarDate(lyear, lmonth, lday, isLeap) {
  var offset = 0;
  
  // increment year
  for(var i = 1900; i < lyear; i++) {
    offset += lYearDays(i);
  }

  // increment month
  // add days in all months up to the current month
  for (var i = 1; i < lmonth; i++) {
    // add extra days for leap month
    if (i == leapMonth(lyear)) {
      offset += leapDays(lyear);
    }
    offset += monthDays(lyear, i);
  }
  // if current month is leap month, add days in normal month
  if (isLeap) {
    offset += monthDays(lyear, i);
  }
   
  // increment 
  offset += parseInt(lday) - 1;

  var baseDate = new Date(1900,0,31);
  var solarDate = new Date(baseDate.valueOf() + offset * 86400000);
  return solarDate;
}


//==============================杩斿洖鍏巻 y骞存煇m+1鏈堢殑澶╂暟
function solarDays(y,m) {
   if(m==1)
      return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28);
   else
      return(solarMonth[m]);
}

//============================== 浼犲叆 offset 杩斿洖骞叉敮, 0=鐢插瓙
function cyclical(num) {
   return(Gan[num%10]+Zhi[num%12]);
}


//============================== 闃村巻灞炴�
function calElement(sYear,sMonth,sDay,week,lYear,lMonth,lDay,isLeap,cYear,cMonth,cDay) {

      this.isToday    = false;
      //鐡ｅ彞
      this.sYear      = sYear;   //鍏厓骞�浣嶆暟瀛�
      this.sMonth     = sMonth;  //鍏厓鏈堟暟瀛�
      this.sDay       = sDay;    //鍏厓鏃ユ暟瀛�
      this.week       = week;    //鏄熸湡, 1涓腑鏂�
      //鍐滃巻
      this.lYear      = lYear;   //鍏厓骞�浣嶆暟瀛�
      this.lMonth     = lMonth;  //鍐滃巻鏈堟暟瀛�
      this.lDay       = lDay;    //鍐滃巻鏃ユ暟瀛�
      this.isLeap     = isLeap;  //鏄惁涓哄啘鍘嗛棸鏈�
      //鍏瓧
      this.cYear      = cYear;   //骞存煴, 2涓腑鏂�
      this.cMonth     = cMonth;  //鏈堟煴, 2涓腑鏂�
      this.cDay       = cDay;    //鏃ユ煴, 2涓腑鏂�

      this.color      = '';

      this.lunarFestival = ''; //鍐滃巻鑺傛棩
      this.solarFestival = ''; //鍏巻鑺傛棩
      this.solarTerms    = ''; //鑺傛皵
}

//===== 鏌愬勾鐨勭n涓妭姘斾负鍑犳棩(浠�灏忓瘨璧风畻)
function sTerm(y,n) {
   var offDate = new Date( ( 31556925974.7*(y-1900) + sTermInfo[n]*60000  ) + Date.UTC(1900,0,6,2,5) );
   return(offDate.getUTCDate());
}





//============================== 杩斿洖闃村巻鎺т欢 (y骞�m+1鏈�
/*
鍔熻兘璇存槑: 杩斿洖鏁翠釜鏈堢殑鏃ユ湡璧勬枡鎺т欢

浣跨敤鏂瑰紡: OBJ = new calendar(骞�闆惰捣绠楁湀);

  OBJ.length      杩斿洖褰撴湀鏈�ぇ鏃�
  OBJ.firstWeek   杩斿洖褰撴湀涓�棩鏄熸湡

  鐢�OBJ[鏃ユ湡].灞炴�鍚嶇О 鍗冲彲鍙栧緱鍚勯」鍊�

  OBJ[鏃ユ湡].isToday  杩斿洖鏄惁涓轰粖鏃�true 鎴�false

  鍏朵粬 OBJ[鏃ユ湡] 灞炴�鍙傝 calElement() 涓殑娉ㄨВ
*/
function calendar(y,m) {

   var sDObj, lDObj, lY, lM, lD=1, lL, lX=0, tmp1, tmp2, tmp3;
   var cY, cM, cD; //骞存煴,鏈堟煴,鏃ユ煴
   var lDPOS = new Array(3);
   var n = 0;
   var firstLM = 0;

   sDObj = new Date(y,m,1,0,0,0,0);    //褰撴湀涓�棩鏃ユ湡

   this.length    = solarDays(y,m);    //鍏巻褰撴湀澶╂暟
   this.firstWeek = sDObj.getDay();    //鍏巻褰撴湀1鏃ユ槦鏈熷嚑

   ////////骞存煴 1900骞寸珛鏄ュ悗涓哄簹瀛愬勾(60杩涘埗36)
   if(m<2) cY=cyclical(y-1900+36-1);
   else cY=cyclical(y-1900+36);
   var term2=sTerm(y,2); //绔嬫槬鏃ユ湡

   ////////鏈堟煴 1900骞�鏈堝皬瀵掍互鍓嶄负 涓欏瓙鏈�60杩涘埗12)
   var firstNode = sTerm(y,m*2) //杩斿洖褰撴湀銆岃妭銆嶄负鍑犳棩寮�
   cM = cyclical((y-1900)*12+m+12);

   //褰撴湀涓�棩涓�1900/1/1 鐩稿樊澶╂暟
   //1900/1/1涓�1970/1/1 鐩稿樊25567鏃� 1900/1/1 鏃ユ煴涓虹敳鎴屾棩(60杩涘埗10)
   var dayCyclical = Date.UTC(y,m,1,0,0,0,0)/86400000+25567+10;

   for(var i=0;i<this.length;i++) {

      if(lD>lX) {
         sDObj = new Date(y,m,i+1);    //褰撴湀涓�棩鏃ユ湡
         lDObj = new Lunar(sDObj);     //鍐滃巻
         lY    = lDObj.year;           //鍐滃巻骞�
         lM    = lDObj.month;          //鍐滃巻鏈�
         lD    = lDObj.day;            //鍐滃巻鏃�
         lL    = lDObj.isLeap;         //鍐滃巻鏄惁闂版湀
         lX    = lL? leapDays(lY): monthDays(lY,lM); //鍐滃巻褰撴湀鏈�悗涓�ぉ

         if(n==0) firstLM = lM;
         lDPOS[n++] = i-lD+1;
      }

      //渚濊妭姘旇皟鏁翠簩鏈堝垎鐨勫勾鏌� 浠ョ珛鏄ヤ负鐣�
      if(m==1 && (i+1)==term2) cY=cyclical(y-1900+36);
      //渚濊妭姘旀湀鏌� 浠ャ�鑺傘�涓虹晫
      if((i+1)==firstNode) cM = cyclical((y-1900)*12+m+13);
      //鏃ユ煴
      cD = cyclical(dayCyclical+i);

      //sYear,sMonth,sDay,week,
      //lYear,lMonth,lDay,isLeap,
      //cYear,cMonth,cDay
      this[i] = new calElement(y, m+1, i+1, nStr1[(i+this.firstWeek)%7],
                               lY, lM, lD++, lL,
                               cY ,cM, cD );
   }

   //鑺傛皵
   tmp1=sTerm(y,m*2  )-1;
   tmp2=sTerm(y,m*2+1)-1;
   this[tmp1].solarTerms = solarTerm[m*2];
   this[tmp2].solarTerms = solarTerm[m*2+1];
   //if(m==3) this[tmp1].color = 'red'; //娓呮槑棰滆壊

   //鍏巻鑺傛棩
   for(i in sFtv)
      if(sFtv[i].match(/^(\d{2})(\d{2})([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            this[Number(RegExp.$2)-1].solarFestival += RegExp.$4 + ' ';
            if(RegExp.$3=='*') this[Number(RegExp.$2)-1].color = 'red';
         }

   //鏈堝懆鑺傛棩
   for(i in wFtv)
      if(wFtv[i].match(/^(\d{2})(\d)(\d)([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            tmp1=Number(RegExp.$2);
            tmp2=Number(RegExp.$3);
            if(tmp1<5)
               this[((this.firstWeek>tmp2)?7:0) + 7*(tmp1-1) + tmp2 - this.firstWeek].solarFestival += RegExp.$5 + ' ';
            else {
               tmp1 -= 5;
               tmp3 = (this.firstWeek+this.length-1)%7; //褰撴湀鏈�悗涓�ぉ鏄熸湡?
               this[this.length - tmp3 - 7*tmp1 + tmp2 - (tmp2>tmp3?7:0) - 1 ].solarFestival += RegExp.$5 + ' ';
            }
         }

   //鍐滃巻鑺傛棩
   for(i in lFtv)
      if(lFtv[i].match(/^(\d{2})(.{2})([\s\*])(.+)$/)) {
         tmp1=Number(RegExp.$1)-firstLM;
         if(tmp1==-11) tmp1=1;
         if(tmp1 >=0 && tmp1<n) {
            tmp2 = lDPOS[tmp1] + Number(RegExp.$2) -1;
            if( tmp2 >= 0 && tmp2<this.length && this[tmp2].isLeap!=true) {
               this[tmp2].lunarFestival += RegExp.$4 + ' ';
               if(RegExp.$3=='*') this[tmp2].color = 'red';
            }
         }
      }


   //澶嶆椿鑺傚彧鍑虹幇鍦�鎴�鏈�
   if(m==2 || m==3) {
      var estDay = new easter(y);
      if(m == estDay.m)
         this[estDay.d-1].solarFestival = this[estDay.d-1].solarFestival+' 澶嶆椿鑺�Easter Sunday';
   }

   //榛戣壊鏄熸湡浜�
   if((this.firstWeek+12)%7==5)
      this[12].solarFestival += '榛戣壊鏄熸湡浜�;

   //浠婃棩
   //if(y==g_tY && m==g_tM) {this[g_tD-1].isToday = true;}

}




//======================================= 杩斿洖璇ュ勾鐨勫娲昏妭(鏄ュ垎鍚庣涓�婊℃湀鍛ㄥ悗鐨勭涓�富鏃�
function easter(y) {

   var term2=sTerm(y,5); //鍙栧緱鏄ュ垎鏃ユ湡
   var dayTerm2 = new Date(Date.UTC(y,2,term2,0,0,0,0)); //鍙栧緱鏄ュ垎鐨勫叕鍘嗘棩鏈熸帶浠�鏄ュ垎涓�畾鍑虹幇鍦�鏈�
   var lDayTerm2 = new Lunar(dayTerm2); //鍙栧緱鍙栧緱鏄ュ垎鍐滃巻

   if(lDayTerm2.day<15) //鍙栧緱涓嬩釜鏈堝渾鐨勭浉宸ぉ鏁�
      var lMlen= 15-lDayTerm2.day;
   else
      var lMlen= (lDayTerm2.isLeap? leapDays(y): monthDays(y,lDayTerm2.month)) - lDayTerm2.day + 15;

   //涓�ぉ绛変簬 1000*60*60*24 = 86400000 姣
   var l15 = new Date(dayTerm2.getTime() + 86400000*lMlen ); //姹傚嚭绗竴娆℃湀鍦嗕负鍏巻鍑犳棩
   var dayEaster = new Date(l15.getTime() + 86400000*( 7-l15.getUTCDay() ) ); //姹傚嚭涓嬩釜鍛ㄦ棩

   this.m = dayEaster.getUTCMonth();
   this.d = dayEaster.getUTCDate();

}

//====================== 涓枃鏃ユ湡===================================//
function cDay(d){
   var s;

   switch (d) {
      case 10:
         s = '鍒濆崄'; break;
      case 20:
         s = '浜屽崄'; break;
         break;
      case 30:
         s = '涓夊崄'; break;
         break;
      default :
         s = nStr2[Math.floor(d/10)];
         s += nStr1[d%10];
   }
   return(s);
}
//===============================rili365.js========================//
var colors = ['#CC3333', '#DD4477', '#994499', '#6633CC', '#336699', '#3366CC',
		'#22AA99', '#329262', '#109618', '#66AA00', '#AAAA11', '#D6AE00',
		'#EE8800', '#DD5511', '#A87070', '#8C6D8C', '#627487', '#7083A8',
		'#5C8D87', '#898951', '#B08B59'];
var options = {
	maxDetailHeight : 350, // 寮瑰嚭灞傛樉绀虹殑鏈�ぇ楂樺害
	maxDetailItems : 20, // 寮瑰嚭灞傞粯璁ゆ樉绀虹殑鏈�ぇ鏉＄洰鏁�
	firstDayOfWeek : 1, // 姣忓懆鐨勭涓�ぉ锛岄粯璁ゅ懆涓�
	schedulePageSize : 20
	// 鏃ョ▼鍒楄〃姣忛〉鏄剧ず鏉＄洰鏁�
};
var global = {
	currYear : -1, // 褰撳墠骞�
	currMonth : -1, // 褰撳墠鏈堬紝0-11
	currDate : null, // 褰撳墠鐐归�鐨勬棩鏈�
	uid : null,
	username : null,
	email : null,
	single : false
	// 鏄惁涓虹嫭绔嬮〉璋冪敤锛屽鏋滄槸鍊间负鏃ュ巻id锛屼娇鐢ㄦ椂璇锋敞鎰忓0鐨勫垽鏂紝浣跨敤 single !== false 鎴栬� single === false
};
// 褰撳墠鐐瑰嚮鐨勬棩鏈�
var day_num;
var monthView;
function MonthViewObj(year, month) {
	this.cld = null; // calendar鐨勫疄渚�
	this.year = year;
	this.month = month;
	this.firstline = 0; // 褰撳墠鏈堢涓�鑳芥樉绀虹殑鏃ユ湡鏁�
	this.linecount = 0; // 褰撳墠鏈堟�鍏遍渶瑕佺殑鏃ユ湡琛屾暟
}
var dateSelection = {
	currYear : -1,
	currMonth : -1,

	minYear : 1901,
	maxYear : 2100,

	beginYear : 0,
	endYear : 0,

	tmpYear : -1,
	tmpMonth : -1,

	init : function(year, month) {
		if (typeof year == 'undefined' || typeof month == 'undefined') {
			year = global.currYear;
			month = global.currMonth;
		}
		
		this.setYear(year);
		this.setMonth(month);
		this.showYearContent();
		this.showMonthContent();
	},
	show : function() {
		$.dom('dateSelectionDiv').style.display = 'block';
	},
	hide : function() {
		this.rollback();
		$.dom('dateSelectionDiv').style.display = 'none';
	},
	today : function() {
		var today = new Date();
		var year = today.getFullYear();
		var month = today.getMonth();

		if (this.currYear != year || this.currMonth != month) {
			if (this.tmpYear == year && this.tmpMonth == month) {
				this.rollback();
			} else {
				this.init(year, month);
				this.commit();
			}
		}
	},
	go : function() {
		if (this.currYear == this.tmpYear && this.currMonth == this.tmpMonth) {
			this.rollback();
		} else {
			this.commit();
		}
		this.hide();
	},
	goToday : function() {
		this.today();
		this.hide();
	},
	goPrevMonth : function() {
		this.prevMonth();
		this.commit();
	},
	goNextMonth : function() {
		this.nextMonth();
		this.commit();
	},
	goPrevYear : function() {
		this.prevYear();
		this.commit();
	},
	goNextYear : function() {
		this.nextYear();
		this.commit();
	},
	changeView : function() {
		global.currYear = this.currYear;
		global.currMonth = this.currMonth;

		loadPage(global.currYear, global.currMonth);
	},
	commit : function() {
		if (this.tmpYear != -1 || this.tmpMonth != -1) {
			// 濡傛灉鍙戠敓浜嗗彉鍖�
			if (this.currYear != this.tmpYear
					|| this.currMonth != this.tmpMonth) {
				// 鎵ц鏌愭搷浣�
				this.showYearContent();
				this.showMonthContent();
				this.changeView();
				/*
				if(global.currYear>2007&&global.currYear<2012) //鏉�
					if (eval("HuangLi.y" + global.currYear) == null) {
						var filename="js/huangli/"+global.currYear+".js";
						loadjscssfile(filename,"js");
					}
				*/
			}
			//add holiday
			thisYear = this.currYear;
			thisMonth = this.currMonth
			$.post("getHolidayOneMonth.action", {holidayType:holidayType,thisMonth:(this.currMonth + 1),thisYear:this.currYear}, function (data) {
				if(data != null && data != ''){
 					var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;
					for (var dateNum in dateMap) {
						var dateCell = dateMap[dateNum];
						date = thisYear+ '-' + fillZeroBellowTwo(thisMonth + 1) + '-' + fillZeroBellowTwo(dateCell.date);
						var cell = $.dom('rg_cell_h' + dateCell.date);
						if(data.indexOf(date)>-1) {
 						    if(data.indexOf(date+'-add,')>-1){
						        initBackGroundAdd(cell.parentNode);
						    }else if(data.indexOf(date+'-remove,')>-1){
						        initBackGroundRemove(cell.parentNode);
						    }else if(data.indexOf(date+',')>-1){
								initBackGround(cell.parentNode);
						    }
						}
					}
				}
			});
			
			this.tmpYear = -1;
			this.tmpMonth = -1;
		}
	},
	rollback : function() {
		if (this.tmpYear != -1) {
			this.setYear(this.tmpYear);
		}
		if (this.tmpMonth != -1) {
			this.setMonth(this.tmpMonth);
		}
		this.tmpYear = -1;
		this.tmpMonth = -1;
		this.showYearContent();
		this.showMonthContent();
	},
	prevMonth : function() {
		var month = this.currMonth - 1;
		if (month == -1) {
			var year = this.currYear - 1;
			if (year >= this.minYear) {
				month = 11;
				this.setYear(year);
			} else {
				month = 0;
			}
		}
		this.setMonth(month);
	},
	nextMonth : function() {
		var month = this.currMonth + 1;
		if (month == 12) {
			var year = this.currYear + 1;
			if (year <= this.maxYear) {
				month = 0;
				this.setYear(year);
			} else {
				month = 11;
			}
		}
		this.setMonth(month);
	},
	prevYear : function() {
		var year = this.currYear - 1;
		if (year >= this.minYear) {
			this.setYear(year);
		}
	},
	nextYear : function() {
		var year = this.currYear + 1;
		if (year <= this.maxYear) {
			this.setYear(year);
		}
	},
	prevYearPage : function() {
		this.endYear = this.beginYear - 1;
		this.showYearContent(null, this.endYear);
	},
	nextYearPage : function() {
		this.beginYear = this.endYear + 1;
		this.showYearContent(this.beginYear, null);
	},
	selectYear : function(){//鏉細select
		var selectY = $('select[@name="SY"] option[@selected]').text();
		this.setYear(selectY);
		this.commit();	
	},
	selectMonth : function(){
		var selectM = $('select[@name="SM"] option[@selected]').text();
		this.setMonth(selectM-1);
		this.commit();	
	},
	setYear : function(value) {
		if (this.tmpYear == -1 && this.currYear != -1) {
			this.tmpYear = this.currYear;
		}
		$('#SY' + this.currYear).removeClass('curr');
		this.currYear = value;
		$('#SY' + this.currYear).addClass('curr');
	},
	setMonth : function(value) {
		if (this.tmpMonth == -1 && this.currMonth != -1) {
			this.tmpMonth = this.currMonth;
		}
		$('#SM' + this.currMonth).removeClass('curr');
		this.currMonth = value;
		$('#SM' + this.currMonth).addClass('curr');
	},
	showYearContent : function(beginYear, endYear) {
		if (!beginYear) {
			if (!endYear) {
				endYear = this.currYear + 1;
			}
			this.endYear = endYear;
			if (this.endYear > this.maxYear) {
				this.endYear = this.maxYear;
			}
			this.beginYear = this.endYear - 3;
			if (this.beginYear < this.minYear) {
				this.beginYear = this.minYear;
				this.endYear = this.beginYear + 3;
			}
		}
		if (!endYear) {
			if (!beginYear) {
				beginYear = this.currYear - 2;
			}
			this.beginYear = beginYear;
			if (this.beginYear < this.minYear) {
				this.beginYear = this.minYear;
			}
			this.endYear = this.beginYear + 3;
			if (this.endYear > this.maxYear) {
				this.endYear = this.maxYear;
				this.beginYear = this.endYear - 3;
			}
		}

		var s = '';
		for (var i = this.beginYear; i <= this.endYear; i++) {
			s += '<span id="SY' + i
					+ '" class="year" onclick="dateSelection.setYear(' + i
					+ ')">' + i + '</span>';
		}
		$.dom('yearListContent').innerHTML = s;
		$('#SY' + this.currYear).addClass('curr');
	},
	showMonthContent : function() {
		var s = '';
		for (var i = 0; i < 12; i++) {
			s += '<span id="SM' + i
					+ '" class="month" onclick="dateSelection.setMonth(' + i
					+ ')">' + (i + 1).toString() + '</span>';
		}
		$.dom('monthListContent').innerHTML = s;
		$('#SM' + this.currMonth).addClass('curr');
	}
};
var utils = {
	numToWeek : function(num) {
		switch (num) {
			case 1 :
				return '涓�;
			case 2 :
				return '浜�;
			case 3 :
				return '涓�;
			case 4 :
				return '鍥�;
			case 5 :
				return '浜�;
			case 6 :
				return '鍏�;
			case 0 :
				return '鏃�;
		}
	},
	getEvent : function(ev) {
		return window.event ? window.event : (ev ? ev : null);
	},
	getMousePosition : function(ev) {
		var evt = this.getEvent(ev);
		if (evt.pageX || evt.pageY) {
			return {
				x : evt.pageX,
				y : evt.pageY
			};
		}
		return {
			x : evt.clientX + document.documentElement.scrollLeft
					- document.documentElement.clientLeft,
			y : evt.clientY + document.documentElement.scrollTop
					- document.documentElement.clientTop
		};
	},
	getClientWidth : function() {
		return $.browser.msie ? ieBody.clientWidth : window.innerWidth;
	},
	getClientHeight : function() {
		return $.browser.msie ? ieBody.clientHeight : window.innerHeight;
	},
	getOffsetXY : function(obj, parentId) {
		/*
		 * 
		 * getOffsetXY 鑾峰彇鐩稿鍧愭爣
		 * 
		 * @param obj id鎴栬�dom瀵硅薄 @param parentId 鐖剁骇id锛屽鏋滀笉鎻愪緵鍒欎负body
		 * 
		 * @return 鍧愭爣瀵硅薄锛寈銆亂
		 * 
		 */
		var element;
		if (typeof obj == 'object') {
			element = obj;
		} else {
			element = document.getElementById(obj);
		}
		var element_X = element.offsetLeft;
		var element_Y = element.offsetTop;
		while (true) {
			if ((!element.offsetParent) || (!element.offsetParent.style)
					|| (!!parentId && element.offsetParent.id == parentId)) {
				break;
			}
			element_X += element.offsetParent.offsetLeft;
			element_Y += element.offsetParent.offsetTop;
			element = element.offsetParent;
		}
		element_X = element_X - document.body.scrollLeft;
		element_Y = element_Y - document.body.scrollTop;

		return {
			x : element_X,
			y : element_Y
		};
	},
	getChinaNum : function(num){
		var cNum = "";
		switch(num){
			case 1 : cNum = "涓� ; break;
			case 2 : cNum = "浜� ; break;
			case 3 : cNum = "涓� ; break;
			case 4 : cNum = "鍥� ; break;
			case 5 : cNum = "浜� ; break;
			case 6 : cNum = "鍏� ; break;
			case 7 : cNum = "涓� ; break;
			case 8 : cNum = "鍏� ; break;
			case 9 : cNum = "涔� ; break;
			case 10 : cNum = "鍗� ; break;
			case 11 : cNum = "鍗佷竴" ; break;
			case 12 : cNum = "鑵� ; break;
			
		}
		return cNum;
	},
	getMonthKey : function(year, month) { // 浼犲叆鐨刴onth涓�-11鐨勬暟鍊�
		return year.toString() + (month + 1).toString().leftpad(2) // 杩斿洖yyyyMM鏍煎紡鐨勫瓧绗︿覆
	}
};

var tplMgr = {
	tplMap : {},
	getInstance : function(id) {
		var instance = this.tplMap[id];
		if (!instance) {
			instance = new jCT($.dom(id).value);
			instance.Build();
			this.tplMap[id] = instance;
		}
		return instance;
	}
};
var cacheMgr = {
	permissions : {}, // 淇濆瓨鏃ュ巻鐨勬潈闄愶紝浠id涓簁ey锛寁alue=permission
	cldCache : {}, // 娉ㄦ剰锛佽繖閲屽瓨鐨勬槸calendarObj.js涓畾涔夌殑calendar瀵硅薄锛屼笉鏄暟鎹枃浠惰浇鍏ョ殑cldObj
	getCld : function(year, month) {
		var key = utils.getMonthKey(year, month);
		var cld = this.cldCache[key];
		if (typeof cld == 'undefined') {
			cld = new calendar(year, month);
			this.cldCache[key] = cld;
		}
		return cld;
	},
	monthViewCache : {},
	getMonthView : function(year, month) {
		var key = utils.getMonthKey(year, month);
		var monthView = this.monthViewCache[key];
		if (typeof monthView == 'undefined') {
			var cld = this.getCld(year, month);
			var firstline = 7 - cld.firstWeek + options.firstDayOfWeek; // 璁＄畻绗竴琛岃兘鏄剧ず鐨勬棩鏈熸暟
			firstline = firstline > 7 ? firstline - 7 : firstline;
			var linecount = Math.ceil((cld.length - firstline) / 7) + 1; // 璁＄畻鎬诲叡闇�鐨勬棩鏈熻鏁�
			monthView = new MonthViewObj(year, month);
			monthView.cld = cld;
			monthView.firstline = firstline;
			monthView.linecount = linecount;
			this.monthViewCache[key] = monthView;
		}

		return monthView;
	},
	monthMap : {},
	getMonthCache : function(year, month) {// yang----
		var cache = this.monthMap[utils.getMonthKey(year, month)];
		if (typeof cache == 'undefined') {
			var view = this.getMonthView(year, month);

			var cld = view.cld;
			var firstline = view.firstline;
			var linecount = view.linecount;
			cache = new MonthCache(year, month);
			for (var i = 0; i < linecount; i++) {
				for (var j = 0; j < 7; j++) {
					var index = i * 7 + j;
					var dayInMonth = true;
					if ((index < (7 - firstline))
							|| (index >= (cld.length - firstline + 7))) {
						dayInMonth = false;
					}

					var dateNum = 0; // 鏈湀鐨勫嚑鍙�
					if (dayInMonth) {
						dateNum = index - 7 + firstline + 1;

						var cell = new DateCell(dateNum, index);
						cache.dateMap[dateNum] = cell;
					}
				}
			}
			this.monthMap[utils.getMonthKey(year, month)] = cache;
		}
		return cache;
	}
};
var tabMgr = {
	switchTab : function(tabId, selectedId) {
		$('#' + tabId + ' li.selected').each(function() {
					$(this).removeClass('selected');
					$('#con_' + $(this).attr('id')).hide();
				});
		$('#' + selectedId).addClass('selected');
		$('#con_' + selectedId).show();
	}
}

function changeBackGround(o){
	o.style.backgroundColor = '';
}
function initBackGroundAdd(o){
	o.style.backgroundColor = FIX_COLORADD;
	//o.style.color = 'red';
}
function initBackGroundRemove(o){
	o.style.backgroundColor = FIX_COLORREMORE;
	//o.style.color = 'red';
}
function initBackGround(o){
	o.style.backgroundColor = FIX_COLOR;
	//o.style.color = 'red';
}
function isBackGroundChanged(o){
	return o.style.backgroundColor == FIX_COLOR;
}
function isBackGroundChangedAdd(o){
	return o.style.backgroundColor == FIX_COLORADD;
}
function isBackGroundChangedRemove(o){
	return o.style.backgroundColor == FIX_COLORREMORE;
}
function DateCell(date, index) {
	this.date = date;
	this.index = index;
	this.scheduleMap = {}; // 褰撳ぉ鐨勬棩绋�
	this.cldIds = {}; // 鍦ㄥ綋澶╂湁鏃ョ▼鐨勬棩鍘咺D锛岀敤浜庣粯鍒跺皬鍥炬爣

	this.displayed = 0;
	this.count = 0;

	this.iconHTML = '';
	this.eventHTML = '';

	this.getClickAreaHTML = function() {
		// 璁＄畻鏃ユ湡鏍煎瓙椤剁偣鍧愭爣
		var zero = utils.getOffsetXY('rg_rowy_h' + this.index, 'decowner');

		var el_rg_rowy = $.dom('rg_rowy_h' + this.index);
		var elCell = el_rg_rowy.parentNode;

		// 鏃ユ湡鏍煎瓙鐨勫ぇ灏�
		var w = elCell.offsetWidth;
		var h = elCell.offsetHeight;

		// 鍒涘缓鍙偣鍑诲尯鍩�
		var instance = tplMgr.getInstance('detail_click_tpl');
		return instance.GetView({
					index : this.index,
					date : this.date,
					left : zero.x,
					top : zero.y -(elCell.offsetHeight - el_rg_rowy.offsetHeight),
					width : elCell.offsetWidth,
					height : elCell.offsetHeight
				});
	}
}

function MonthCache(year, month) {
	this.year = year;
	this.month = month;
	this.dateMap = {};
}
function loadPage(year, month) {
	// 鍔犺浇涓婚
	var theme = new Theme();
	singleMgr.appendTheme(theme);
	// alert("loadPage");
	monthView = cacheMgr.getMonthView(year, month);

	$.dom('yearValue').innerHTML = year.toString();
	$.dom('monthValue').innerHTML = (month + 1).toString().leftpad(2);

	$.dom('lunarValue').innerHTML = '鍐滃巻 '
			+ cyclical(global.currYear - 1900 + 36) + '骞�銆�
			+ Animals[(global.currYear - 4) % 12] + '骞淬�';

	pageMgrRili.drawCalendar();
	pageMgrRili.adjustGridContainer();
}

function Theme() {
	this.bgColor = '#6b92d7';
	this.bgColor2 = '#ecf1ff';
	this.bgColor1 = '#c2cff1';//'#d9e1f4';
	this.bgColor3 = '#ffffff';
}

var indexMgr = {
	dateover : function(index,dataNum,event) {
		var dateTitle = $.dom('rg_rowy_h' + index);
		//var dateClick = $.dom('click_Num'+index);
		var dateCell = dateTitle.parentNode;
		if(dateCell.style.backgroundColor != FIX_COLOR){
			//dateTitle.style.backgroundColor = '#FBFFBC'; 
			//dateCell.style.backgroundColor = '#FBFFBC';
		}
		//dateClick.style.backgroundColor = '#FBFFBC';  //缁欏浘鐗囨坊鍔爃over鏁堟灉
		//showDetailsRili(event,dataNum);
	},
	dateout : function(index) {
		var dateTitle = $.dom('rg_rowy_h' + index);
		//var dateClick = $.dom('click_Num'+index);
		var dateCell = dateTitle.parentNode;
		if(dateCell.style.backgroundColor != FIX_COLOR){
			//dateTitle.style.backgroundColor = '';
			//dateCell.style.backgroundColor = '';
		}
		//dateClick.style.backgroundColor = '';
		//dialogMgrRili.hide();
	},
	addHoilday: function(index){
		// add holidays
		var dateCell = $.dom('rg_cell_h' + index);
		var removeType = "remove";
		var thisDate = global.currYear+'-'+fillZeroBellowTwo(global.currMonth+1) + "-" + fillZeroBellowTwo(dateCell.innerText);
		if(isBackGroundChanged(dateCell.parentNode)||isBackGroundChangedAdd(dateCell.parentNode)||isBackGroundChangedRemove(dateCell.parentNode)){
		    if(isBackGroundChangedAdd(dateCell.parentNode)){
		        removeType = "removeAdd" ;
		    }else if(isBackGroundChangedRemove(dateCell.parentNode)){
		        removeType = "removeRemove" ;
		    }
			$.post("setThis2Holiday.action", {holidayType:holidayType,removeType:removeType,thisDate:thisDate,method:"remove"}, function (data) {
				if(data == 'ok'){
					changeBackGround(dateCell.parentNode);
				}else if(data == 'okAdd'){
				    initBackGroundAdd(dateCell.parentNode);
				}else if(data == 'okRemove'){
				    initBackGroundRemove(dateCell.parentNode);
				}else if(data=='removeRemove'){
				    initBackGround(dateCell.parentNode);
				}else if(data=='removeAdd'){
					changeBackGround(dateCell.parentNode);
				}else{
					alert(data);
				}
			});
		}else{
			$.post("setThis2Holiday.action", {holidayType:holidayType,thisDate:thisDate,method:"add"}, function (data) {
				if(data == 'ok'){
					initBackGround(dateCell.parentNode);
				}else if(data == 'okAdd'){
                    initBackGroundAdd(dateCell.parentNode);
                }else if(data == 'okRemove'){
                    initBackGroundRemove(dateCell.parentNode);
                }else{
                    alert(data);
                }
			});
		}
		
	},
	removeHoilday :function(index){
		var dateCell = $.dom('rg_cell_h' + index);
		dateCell.style.color='white';
	
	}
};

var singleMgr = {
	appendTheme : function(theme) {
		$('.themeBgColor').each(function() {
					$(this).css('background-color', theme.bgColor);
				});
		$('.themeBgColor1').each(function() {
					$(this).css('background-color', theme.bgColor1);
				});
		$('.themeBgColorRili').each(function() {
					$(this).css('background-color', theme.bgColor2);
				});
		$('.themeBgColorRili1').each(function() {
					$(this).css('background-color', theme.bgColor3);
				});
	}
};
function gotoWeb(cid, sid) {
	window.open("/link.jsp?cid=" + cid + "&sid=" + sid + "&refer="
			+ escape(window.location));
}
function initRiliIndex() {
	var dateObj = new Date();
	global.currYear = dateObj.getFullYear();
	global.currMonth = dateObj.getMonth();
	loadPage(global.currYear, global.currMonth);
	dateSelection.init();
	hoverDialog();
	initHolidays(global.currYear, global.currMonth);
	jumpToEarlyMonth();
}
function jumpToEarlyMonth(){
	if(SHOW_HOLIDAY_MONTH){
		$.post("getMostEarlyMonth.action", {holidayType:holidayType,thisYear:global.currYear,thisMonth:global.currMonth}, function (data) {
			//earlyMonth = parseInt(data) > 0 ? parseInt(data) - 1 : global.currMonth;
			var array = data.split('-');
			dateSelection.setYear(parseInt(array[0]));
			dateSelection.setMonth(parseInt(array[1]));
			dateSelection.commit();
		});
	}
}
function initHolidays(thisYear, thisMonth){
	$.post("getHolidayOneMonth.action", {holidayType:holidayType,thisMonth:(thisMonth + 1),thisYear:thisYear}, function (data) {
		if(data != null && data != ''){
			var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;
			for (var dateNum in dateMap) {
				var dateCell = dateMap[dateNum];
				date = thisYear+ '-' + fillZeroBellowTwo(thisMonth + 1) + '-' + fillZeroBellowTwo(dateCell.date);
				//var cell = $.dom('rg_cell_h' + dateCell.date);
				var el_rg_rowy = $.dom('rg_rowy_h' + dateCell.index);
				var cell = el_rg_rowy.parentNode;
				//if(data.indexOf(date + ',')>-1) {
				if(data.indexOf(date )>-1) {
				    //alert(data);
				    if(data.indexOf(date+'-add,')>-1){
				        initBackGroundAdd(cell);
				    }else if(data.indexOf(date+'-remove,')>-1){
				        initBackGroundRemove(cell);
				    }else if(data.indexOf(date+',')>-1){
						initBackGround(cell);
				    }
				}
			}
		}
	});
}
function fillZeroBellowTwo(a){
	if(a < 10){
		return '0' + a;
	}
	return a;
}
var dateDetailNum;
function showDetailsRili(event,dateNum) {
	var op = {
		width : 328
	};
	var cld = cacheMgr.getCld(global.currYear, global.currMonth);
	var cld_day = cld[dateNum - 1];
	op.title = cld_day.sYear + '骞� + cld_day.sMonth + '鏈� + cld_day.sDay
			+ '鏃�鏄熸湡' + cld_day.week;
	var key = 'd'+global.currYear+'-'+(global.currMonth+1)+'-'+dateNum;
	if(cellImage[key]){
		op.desc = cellImage[key].imageDesc;
	}
	else			//鏉細鑺傛皵
	{
		var s;
		s=cld_day.lunarFestival;
      	if(s.length>0) { //鍐滃巻鑺傛棩
      		op.desc=s;
      	}
      	else { //寤垮洓鑺傛皵
        	s=cld_day.solarTerms;
        	if(s.length>0){
        	op.desc=s;	
        }
        else { //鍏巻鑺傛棩
          s=cld_day.solarFestival;
          if(s.length>0) {
           op.desc=s;
          }
        }
      }
	}
	var detailInfo = {
		year : cld_day.sYear,
		month : cld_day.sMonth,
		dayOfMonth : dateNum
	};
	var evt = utils.getMousePosition(event);
    var	x = evt.x;
	var y = evt.y;
	detailInfo.lunar = '鍐滃巻' + (cld_day.isLeap ? '闂�' : '') + cld_day.lMonth
			+ '鏈� + cDay(cld_day.lDay) + '&nbsp;&nbsp;' + cld_day.cYear + '骞�'
			+ cld_day.cMonth + '鏈�' + cld_day.cDay + '鏃�;
	detailInfo.dateNum = dateNum;
	detailInfo.dateDetail = op.title;
	try {
		var hl = eval('HuangLi.y'
				+ global.currYear
				+ '.d'
				+ (cld_day.sMonth < 10
						? ('0' + cld_day.sMonth)
						: cld_day.sMonth)
				+ (cld_day.sDay < 10 ? ('0' + cld_day.sDay) : cld_day.sDay));
		detailInfo.huangliY = hl.y;
		detailInfo.huangliJ = hl.j;
		if(indexDataSchedule[key])
		detailInfo.schedule = indexDataSchedule[key];
	} catch (e) {
	}
	var elDetail = $.dom('detail');
	
	elDetail.innerHTML = tplMgr.getInstance('detail_default_tpl').GetView(
			detailInfo);
	dialogMgrRili.show('detailDialog', op,x,y,event);
}
function showIframeRili(d)
{    
    if(typeof(READ_ONLY)!='undefined'&&READ_ONLY)
       return;
    if(document.getElementById("unAudit").value=='true'){
	    alert("姝よ妭鍋囨棩鏈鏍革紝鏃犳硶淇敼銆�);
	    return;
    }
	//$("#calendar_container").slideDown("slow");
	dateDetailNum = d;
	var cld = cacheMgr.getCld(global.currYear, global.currMonth);
	var cld_day = cld[d - 1];
	var mth='';
	var day='';
	
	mth=cld_day.sMonth<10?'0'+cld_day.sMonth:''+cld_day.sMonth;
	day=d<10?'0'+d:''+d;
	//alert(cld_day.sYear + '骞� + mth + '鏈� + day+ '鏃�);
	indexMgr.addHoilday(d);
	
	return;
}
function showCalendars()
{
	$("#iframeContainer").fadeOut("fast",function(){
		$("#calendar_container").fadeIn("slow");
	});
}
var dialogMgrRili = {
	dialog: null,
	option: null,
	moving: false,
	pos: null,
	
	show: function(el, options,x,y,event){
		var op = {
			width: 328,
			title: '',
			draggable: true,		//榛樿瀵硅瘽妗嗗厑璁哥偣鍑绘爣棰樻爮鎷栧姩
			desc:''
		};
		var options = options || {};
		for (var p in options) {
			op[p] = options[p];
		}
		
		if (this.dialog) {
			this.hide();
		}
		
		this.option = op;
		
		this.dialog = $.dom(el);
		var width = this.option.width;
		var w = utils.getClientWidth();
		var h = utils.getClientHeight();
		var deatilHeight = 180;//瀵硅瘽妗嗙殑楂樺害
		
		var left = 0;
		
		if (w > width) {
			left = (w - width) / 2;
		}

		left = (x-w/2)>0?(x-width-8):x;
		//left = (x-width/2>0)?(x-width/2):x;
		this.dialog.style.left = left + 'px';

		var top = (y-h/2>0)?(y-deatilHeight-28):(y+8);
		//var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
		//var top = y+10;
		this.dialog.style.top = top + 'px';
		
		var shadowHeight = $.dom('page').offsetHeight > utils.getClientHeight() ? $.dom('page').offsetHeight : utils.getClientHeight();
		var d = document;
		var pos={ x:x,y:y};
		d.onmousemove = function(ev){
					try{
					var p = utils.getMousePosition(ev);
					var left1 = parseInt(dialogMgrRili.dialog.style.left);
					var top1 = parseInt(dialogMgrRili.dialog.style.top);
					
					left1 += p.x - pos.x;
					top1 += p.y - pos.y;
					top = top>p.y?top:p.y+1;
					dialogMgrRili.dialog.style.left = left1 + 'px';
					dialogMgrRili.dialog.style.top = top1 + 'px';
					pos = p;
					}
					catch(e){}
				};
		$(this.dialog).find('.title').html(this.option.desc).end().show();
	},
	hide: function(){
		//hideQuickAdd();//kun:ugly浠ｇ爜
		if (this.dialog) {
			$(this.dialog).hide();
			if (this.option.hideCallback) {
				try {
					this.option.hideCallback();
				} 
				catch (ex) {
				}
			}
			this.option = null;
			this.dialog = null;
			this.moving = false;
			this.pos = null;
		}
	}
};
var pageMgrRili = {
	resizing : false,
	adjustGridContainer : function() {
		var elMainWrapper = $.dom('mainWrapper');
		var elMainNav = $.dom('mainNavRili');
		var elColheaders = $.dom('colheadersRili');
		var elGridContainer = $.dom('gridcontainerRili');

		elGridContainer.style.height = 300 + 'px';
		//elGridContainer.style.height = 300+'px';
		var pos = utils.getOffsetXY('dateSelectionRili', 'mainBody');

		var dateSelectionDiv = $.dom('dateSelectionDiv');
		dateSelectionDiv.style.left = pos.x + 'px';

		if (monthView && monthView.ready) {
			pageMgrRili.drawClickArea();
			//showSchedules();
		}
	},
	drawWeekLine : function() { // 缁樺埗鍛ㄦ爣棰�
		var width = Math.round(1 / 7 * 1000000) / 10000;
		var buffer = new StringBuffer();
		for (var i = 0; i < 7; i++) { // draw weekline
			var weeknum = (options.firstDayOfWeek + i) % 7;
			var left = Math.round(i / 7 * 1000000) / 10000;
			if(utils.numToWeek(weeknum)=='鍏�||utils.numToWeek(weeknum)=='鏃�)
			{
				buffer.append('<div style="width: ').append(width)
					.append('%; left: ').append(left)
					.append('%;color:red;" class="chead cheadNotTodayRili"><span id="chead')
					.append(i).append('">鏄熸湡').append(utils.numToWeek(weeknum))
					.append('</span></div>');
			}
			else{
			buffer.append('<div style="width: ').append(width)
					.append('%; left: ').append(left)
					.append('%;" class="chead cheadNotTodayRili"><span id="chead')
					.append(i).append('">鏄熸湡').append(utils.numToWeek(weeknum))
					.append('</span></div>');
			}
		}
		$.dom('colheadersRili').innerHTML = buffer.toString();
	},
	drawRowSpliter : function() { // 缁樺埗琛屽垎闅旂嚎
		var linecount = monthView.linecount;
		var buffer = new StringBuffer();
		for (var i = 0; i < linecount; i++) {
			var top = Math.round(i / linecount * 1000000) / 10000;
			if (i > 0) {
				buffer.append('<div id="r').append(i)
						.append('" class="hrule hruleMonth" style="top: ')
						.append(top).append('%;"></div>');
			}
		}
		$.dom('rowowner').innerHTML = buffer.toString();
	},
	drawColSpliter : function() { // 缁樺埗鍒楅棿闅旂嚎
		var buffer = new StringBuffer();
		for (var i = 1; i < 7; i++) {
			var left = Math.round(i / 7 * 1000000) / 10000;
			buffer
					.append('<div id="c')
					.append(i)
					.append('" class="vrule nogutterRili" style="width: 1px; left: ')
					.append(left)
					.append('%; height: 100%;"></div>');
		}
		$.dom('colowner').innerHTML = buffer.toString();
	},
	drawClickArea : function() {
		var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;

		var buffer = new StringBuffer();
		for (var dateNum in dateMap) {
			var dateCell = dateMap[dateNum];
			buffer.append(dateCell.getClickAreaHTML());
		}

		$.dom('clickowner').innerHTML = buffer.toString();
	},
	drawDateGrid : function() { // 缁樺埗鏃ユ湡鏍煎瓙
		monthView.ready = false; // 鏃ュ巻鏈堣鍥炬槸鍚﹀噯澶囧ソ

		var year = monthView.year;
		var month = monthView.month;
		var cld = monthView.cld;
		var firstline = monthView.firstline;
		var linecount = monthView.linecount;

		var width = Math.round(1 / 7 * 1000000) / 10000;

		var todayObj = new Date();
		var isThisMonth = todayObj.getFullYear() == year
				&& todayObj.getMonth() == month;
		var today = todayObj.getDate();

		var buffer = new StringBuffer();
		var height = Math.round(1 / linecount * 1000000) / 10000;

		var in_instance = tplMgr.getInstance('calendar_cell_in_tpl');
		var out_instance = tplMgr.getInstance('calendar_cell_out_tpl');
		var today_instance = tplMgr.getInstance('calendar_cell_today_tpl');
		for (var i = 0; i < linecount; i++) {
			var top = Math.round(i / linecount * 1000000) / 10000;
			for (var j = 0; j < 7; j++) {
				var index = i * 7 + j;
				var currW = '14.2857';
				var dayInMonth = true;
				if ((index < (7 - firstline))
						|| (index >= (cld.length - firstline + 7))) {
					dayInMonth = false;
				}

				var dateNum = 0; // 鏈湀鐨勫嚑鍙�
				var lunar = '';
				var lunarColor = '';
				if (dayInMonth) {
					dateNum = index - 7 + firstline + 1;
					var cld_day = cld[dateNum - 1];
					var s;
//					lunar = cld_day.solarTerms;
//					//alert(lunar);
//					if (lunar.length > 0) {
//						lunarColor = 'red';
//					} else {
//						lunar = cld_day.lDay == 1
//								? (cld_day.isLeap ? '闂� : '')
//										+ cld_day.lMonth
//										+ '鏈�
//										+ (monthDays(cld_day.lYear,
//												cld_day.lMonth) == 29
//												? '灏�
//												: '澶�)
//								: cDay(cld_day.lDay);
//					}
	 if(cld_day.lDay==1) //鏄剧ず鍐滃巻鏈�
        lunar = (cld_day.isLeap?'闂�:'') + cld_day.lMonth + '鏈� + (monthDays(cld_day.lYear,cld_day.lMonth)==29?'灏�:'澶�);
      else //鏄剧ず鍐滃巻鏃�
        lunar = cDay(cld_day.lDay);

      s=cld_day.lunarFestival;
      if(s.length>0) { //鍐滃巻鑺傛棩
        if(s.length>6) s = s.substr(0, 4)+'...';
        lunarColor = "#32CD32";
      }
      else { //寤垮洓鑺傛皵
        s=cld_day.solarTerms;
        if(s.length>0){
          lunarColor = "#32CD32";             
          if((s =='娓呮槑')||(s =='鑺掔')||(s =='澶忚嚦')||(s =='鍐嚦')){
            lunarColor = "#32CD32";
            if(s =='娓呮槑') s = '娓呮槑鑺�;
          }             
        }
        else { //鍏巻鑺傛棩
          s=cld_day.solarFestival;
          if(s.length>0) {
            if(s.length>6) s = s.substr(0, 4)+'...';
            lunarColor = "#46BAEC";
          }
        }
      }
      if(s.length>0) lunar = s;
				}

				var left = Math.round(j / 7 * 1000000) / 10000;

				var isToday = isThisMonth
						&& (today == index - 7 + firstline + 1);
				
				var tpl_data = {
					index : index,
					dateNum : dateNum,
					isToday : isToday,
					lunar : lunar,
					lunarColor : lunarColor,
					left : left,
					top : top,
					width : currW,
					height : height
				}
				if (isToday) {
					buffer.append(today_instance.GetView(tpl_data));
				}
				if (dayInMonth) {
					buffer.append(in_instance.GetView(tpl_data));
				} else {
					buffer.append(out_instance.GetView(tpl_data));
				}
			}
		}

		$.dom('decowner').innerHTML = buffer.toString();
		monthView.ready = true; // 鏈堣鍥惧噯澶囧畬鎴�
	},
		addHoverBtn:function(){
		var config = {
							sensitivity : 3, // number = sensitivity
							interval : 10, // number = milliseconds for
							// onMouseOver polling interval
							over : this.overBtn, // function
							timeout : 10, // number = milliseconds delay
							// before onMouseOut
							out : this.outBtn
							// function = onMouseOut callback (REQUIRED)
						};
						
			$('#mainNav .dateNavBtnWrapperRili')
								.hoverIntent(config);
	},
	overBtn:function(){
		$(this).find('.t2').removeClass('themeBgColorRili1').css('background-color', '#6b92d7');
		$(this).find('.getBtn').removeClass('themeBgColorRili1').removeClass('dateNavBtnRili').addClass('dateNavBtnRili1');
		$(this).find('.getBtn').css('background-color','#6b92d7');

	},
	outBtn:function(){
		$(this).find('.t2').removeClass('themeBgColor').css('background-color', '#ffffff');
		$(this).find('.getBtn').removeClass('themeBgColor').removeClass('dateNavBtnRili1').addClass('dateNavBtnRili');
		$(this).find('.getBtn').css('background-color','#ffffff');
	},
	drawCalendar : function() { // 缁樺埗鏁翠釜鏃ュ巻
		this.drawWeekLine();
		this.drawColSpliter();
		this.drawRowSpliter();
		this.drawDateGrid();
		this.addHoverBtn();
	}
};
function hoverDialog()
{
		$(".title_class").hover(
		 function () {
    	$(this).css("color","red");
  		},
  		function () {
    		$(this).css("color","#000000");
  		}
	);
	$(".leftImg").hover(
		function(){
			$("#play_prev").attr("src","../img/leftImg2.gif");
		},
		function(){
			$("#play_prev").attr("src","../img/leftImg1.gif");
		}
	);
	$(".rightImg").hover(
		function(){
			$("#play_next").attr("src","../img/rightImg1.gif");
		},
		function(){
			$("#play_next").attr("src","../img/rightImg2.gif");
		}
	);
}
//======================================jquery.scrollTo-min.js============//
/**
 * jQuery.ScrollTo - Easy element scrolling using jQuery.
 * Copyright (c) 2007-2008 Ariel Flesler - aflesler(at)gmail(dot)com | http://flesler.blogspot.com
 * Dual licensed under MIT and GPL.
 * Date: 2/19/2008
 * @author Ariel Flesler
 * @version 1.3.3
 *
 * http://flesler.blogspot.com/2007/10/jqueryscrollto.html
 */
;(function($){var o=$.scrollTo=function(a,b,c){o.window().scrollTo(a,b,c)};o.defaults={axis:'y',duration:1};o.window=function(){return $($.browser.safari?'body':'html')};$.fn.scrollTo=function(l,m,n){if(typeof m=='object'){n=m;m=0}n=$.extend({},o.defaults,n);m=m||n.speed||n.duration;n.queue=n.queue&&n.axis.length>1;if(n.queue)m/=2;n.offset=j(n.offset);n.over=j(n.over);return this.each(function(){var a=this,b=$(a),t=l,c,d={},w=b.is('html,body');switch(typeof t){case'number':case'string':if(/^([+-]=)?\d+(px)?$/.test(t)){t=j(t);break}t=$(t,this);case'object':if(t.is||t.style)c=(t=$(t)).offset()}$.each(n.axis.split(''),function(i,f){var P=f=='x'?'Left':'Top',p=P.toLowerCase(),k='scroll'+P,e=a[k],D=f=='x'?'Width':'Height';if(c){d[k]=c[p]+(w?0:e-b.offset()[p]);if(n.margin){d[k]-=parseInt(t.css('margin'+P))||0;d[k]-=parseInt(t.css('border'+P+'Width'))||0}d[k]+=n.offset[p]||0;if(n.over[p])d[k]+=t[D.toLowerCase()]()*n.over[p]}else d[k]=t[p];if(/^\d+$/.test(d[k]))d[k]=d[k]<=0?0:Math.min(d[k],h(D));if(!i&&n.queue){if(e!=d[k])g(n.onAfterFirst);delete d[k]}});g(n.onAfter);function g(a){b.animate(d,m,n.easing,a&&function(){a.call(this,l)})};function h(D){var b=w?$.browser.opera?document.body:document.documentElement:a;return b['scroll'+D]-b['client'+D]}})};function j(a){return typeof a=='object'?a:{top:a,left:a}}})(jQuery);
//==========================================jquery.serialScroll-min.js=================//
/**
 * jQuery[a] - Animated scrolling of series
 * Copyright (c) 2007-2008 Ariel Flesler - aflesler(at)gmail(dot)com | http://flesler.blogspot.com
 * Dual licensed under MIT and GPL.
 * Date: 3/20/2008
 * @author Ariel Flesler
 * @version 1.2.1
 *
 * http://flesler.blogspot.com/2008/02/jqueryserialscroll.html
 */
;(function($){var a='serialScroll',b='.'+a,c='bind',C=$[a]=function(b){$.scrollTo.window()[a](b)};C.defaults={duration:1e3,axis:'x',event:'click',start:0,step:1,lock:1,cycle:1,constant:1};$.fn[a]=function(y){y=$.extend({},C.defaults,y);var z=y.event,A=y.step,B=y.lazy;return this.each(function(){var j=y.target?this:document,k=$(y.target||this,j),l=k[0],m=y.items,o=y.start,p=y.interval,q=y.navigation,r;if(!B)m=w();if(y.force)t({},o);$(y.prev||[],j)[c](z,-A,s);$(y.next||[],j)[c](z,A,s);if(!l.ssbound)k[c]('prev'+b,-A,s)[c]('next'+b,A,s)[c]('goto'+b,t);if(p)k[c]('start'+b,function(e){if(!p){v();p=1;u()}})[c]('stop'+b,function(){v();p=0});k[c]('notify'+b,function(e,a){var i=x(a);if(i>-1)o=i});l.ssbound=1;if(y.jump)(B?k:w())[c](z,function(e){t(e,x(e.target))});if(q)q=$(q,j)[c](z,function(e){e.data=Math.round(w().length/q.length)*q.index(this);t(e,this)});function s(e){e.data+=o;t(e,this)};function t(e,a){if(!isNaN(a)){e.data=a;a=l}var c=e.data,n,d=e.type,f=y.exclude?w().slice(0,-y.exclude):w(),g=f.length,h=f[c],i=y.duration;if(d)e.preventDefault();if(p){v();r=setTimeout(u,y.interval)}if(!h){n=c<0?0:n=g-1;if(o!=n)c=n;else if(!y.cycle)return;else c=g-n-1;h=f[c]}if(!h||d&&o==c||y.lock&&k.is(':animated')||d&&y.onBefore&&y.onBefore.call(a,e,h,k,w(),c)===!1)return;if(y.stop)k.queue('fx',[]).stop();if(y.constant)i=Math.abs(i/A*(o-c));k.scrollTo(h,i,y).trigger('notify'+b,[c])};function u(){k.trigger('next'+b)};function v(){clearTimeout(r)};function w(){return $(m,l)};function x(a){if(!isNaN(a))return a;var b=w(),i;while((i=b.index(a))==-1&&a!=l)a=a.parentNode;return i}})}})(jQuery);

//==================rili_index.js============================//
jQuery(function( $ ){
	$('#downLoad').serialScroll({
		target:'#imgDiv',
		items:'li', // Selector to the items ( relative to the matched elements, '#sections' in this case )
		prev:'#play_prev',// Selector to the 'prev' button (absolute!, meaning it's relative to the document)
		next:'#play_next',// Selector to the 'next' button (absolute too)
		axis:'xy',// The default is 'y' scroll on both ways
		duration:700,// Length of the animation (if you scroll 2 axes and use queue, then each axis take half this time)
		force:true, // Force a scroll to the element specified by 'start' (some browsers don't reset on refreshes)
		onBefore:function( e, elem, $pane, $items, pos ){
			e.preventDefault();
			if( this.blur )
				this.blur();
		},
		onAfter:function( elem ){
			//'this' is the element being scrolled ($pane) not jqueryfied
		}
	})
});
function createXmlhttp(){
  var xmlhttp=null;
  if(window.XMLHttpRequest){
	  xmlhttp = new XMLHttpRequest();
	  if (xmlhttp.overrideMimeType){
	    xmlhttp.overrideMimeType("text/xml");
	  }
  }
  else if(window.ActiveXObject){
	  try{
	    xmlhttp = new  ActiveXObject("Msxml2.XMLHTTP");
	  }catch(e){
	    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	  }
  }
  if(!xmlhttp){
    window.alert("Your broswer not support XMLHttpRequest!");
  }
  return xmlhttp;
}
//鍚屾鑾峰彇url鍐呭
function getUrlContent(surl){
  //window.alert("get:"+surl);
	var xmlhttp = createXmlhttp();
	var rsContent = null;
	xmlhttp.open("GET", surl, false); 
	xmlhttp.setRequestHeader("If-Modified-Since","Last-Modified");
	xmlhttp.send(null);
  if(xmlhttp.readyState == 4){
    if(xmlhttp.status == 200 || xmlhttp.status == 304){
      rsContent = xmlhttp.responseText;  
      return rsContent;
    }
    else{
      return null;
    }
  }
}
//鍔ㄦ�load js鏂囦欢
function loadjscssfile(filename, filetype){
	if (filetype == "js") { //鍒ゆ柇鏂囦欢绫诲瀷 
		var fileref = document.createElement('script');//鍒涘缓鏍囩 
		fileref.setAttribute("language", "javascript");
		fileref.setAttribute("type", "text/javascript");//瀹氫箟灞炴�type鐨勫�涓簍ext/javascript 
		//fileref.setAttribute("src", filename);//鏂囦欢鐨勫湴鍧�
		fileref.text = getUrlContent(filename);
	}
	else 
		if (filetype == "css") { //鍒ゆ柇鏂囦欢绫诲瀷 
			var fileref = document.createElement("link");
			fileref.setAttribute("rel", "stylesheet");
			fileref.setAttribute("type", "text/css");
			fileref.setAttribute("href", filename);
		}
	if (typeof fileref != "undefined") 
		document.getElementsByTagName("head")[0].appendChild(fileref);
}