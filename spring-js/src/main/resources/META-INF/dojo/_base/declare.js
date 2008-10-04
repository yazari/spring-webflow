/*
	Copyright (c) 2004-2007, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/book/dojo-book-0-9/introduction/licensing
*/


if(!dojo._hasResource["dojo._base.declare"]){dojo._hasResource["dojo._base.declare"]=true;dojo.provide("dojo._base.declare");dojo.require("dojo._base.lang");dojo.declare=function(_1,_2,_3){if(dojo.isFunction(_3)||(arguments.length>3)){dojo.deprecated("dojo.declare: for class '"+_1+"' pass initializer function as 'constructor' property instead of as a separate argument.","","1.0");var c=_3;_3=arguments[3]||{};_3.constructor=c;}var dd=arguments.callee,_6=null;if(dojo.isArray(_2)){_6=_2;_2=_6.shift();}if(_6){for(var i=0,m;i<_6.length;i++){m=_6[i];if(!m){throw ("Mixin #"+i+" to declaration of "+_1+" is null. It's likely a required module is not loaded.");}_2=dd._delegate(_2,m);}}var _9=(_3||0).constructor,_a=dd._delegate(_2),fn;for(var i in _3){if(dojo.isFunction(fn=_3[i])&&(!0[i])){fn.nom=i;}}dojo.extend(_a,{declaredClass:_1,_constructor:_9,preamble:null},_3||0);_a.prototype.constructor=_a;return dojo.setObject(_1,_a);};dojo.mixin(dojo.declare,{_delegate:function(_c,_d){var bp=(_c||0).prototype,mp=(_d||0).prototype;var _10=dojo.declare._makeCtor();dojo.mixin(_10,{superclass:bp,mixin:mp,extend:dojo.declare._extend});if(_c){_10.prototype=dojo._delegate(bp);}dojo.extend(_10,dojo.declare._core,mp||0,{_constructor:null,preamble:null});_10.prototype.constructor=_10;_10.prototype.declaredClass=(bp||0).declaredClass+"_"+(mp||0).declaredClass;return _10;},_extend:function(_11){for(var i in _11){if(dojo.isFunction(fn=_11[i])&&(!0[i])){fn.nom=i;}}dojo.extend(this,_11);},_makeCtor:function(){return function(){this._construct(arguments);};},_core:{_construct:function(_13){var c=_13.callee,s=c.superclass,ct=s&&s.constructor,m=c.mixin,mct=m&&m.constructor,a=_13,ii,fn;if(a[0]){if((fn=a[0]["preamble"])){a=fn.apply(this,a)||a;}}if(fn=c.prototype.preamble){a=fn.apply(this,a)||a;}if(ct&&ct.apply){ct.apply(this,a);}if(mct&&mct.apply){mct.apply(this,a);}if(ii=c.prototype._constructor){ii.apply(this,_13);}if(this.constructor.prototype==c.prototype&&(ct=this.postscript)){ct.apply(this,_13);}},_findMixin:function(_1c){var c=this.constructor,p,m;while(c){p=c.superclass;m=c.mixin;if(m==_1c||(m instanceof _1c.constructor)){return p;}if(m&&(m=m._findMixin(_1c))){return m;}c=p&&p.constructor;}},_findMethod:function(_20,_21,_22,has){var p=_22,c,m,f;do{c=p.constructor;m=c.mixin;if(m&&(m=this._findMethod(_20,_21,m,has))){return m;}if((f=p[_20])&&(has==(f==_21))){return p;}p=c.superclass;}while(p);return !has&&(p=this._findMixin(_22))&&this._findMethod(_20,_21,p,has);},inherited:function(_28,_29,_2a){var a=arguments;if(!dojo.isString(a[0])){_2a=_29;_29=_28;_28=_29.callee.nom;}var c=_29.callee,p=this.constructor.prototype,a=_2a||_29,fn,mp;if(this[_28]!=c||p[_28]==c){mp=this._findMethod(_28,c,p,true);if(!mp){throw (this.declaredClass+": name argument (\""+_28+"\") to inherited must match callee (declare.js)");}p=this._findMethod(_28,c,mp,false);}fn=p&&p[_28];if(!fn){console.debug(mp.declaredClass+": no inherited \""+_28+"\" was found (declare.js)");return;}return fn.apply(this,a);}}});}