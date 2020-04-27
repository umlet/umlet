var $wnd = $wnd || window.parent;
var __gwtModuleFunction = $wnd.baseletgwt;
var $sendStats = __gwtModuleFunction.__sendStats;
$sendStats('moduleStartup', 'moduleEvalStart');
var $gwt_version = "2.8.2";
var $strongName = 'FCE1D02C0FB1AF31ADA307E099CC2BAA';
var $gwt = {};
var $doc = $wnd.document;
var $moduleName, $moduleBase;
function __gwtStartLoadingFragment(frag) {
var fragFile = 'deferredjs/' + $strongName + '/' + frag + '.cache.js';
return __gwtModuleFunction.__startLoadingFragment(fragFile);
}
function __gwtInstallCode(code) {return __gwtModuleFunction.__installRunAsyncCode(code);}
function __gwt_isKnownPropertyValue(propName, propValue) {
return __gwtModuleFunction.__gwt_isKnownPropertyValue(propName, propValue);
}
function __gwt_getMetaProperty(name) {
return __gwtModuleFunction.__gwt_getMetaProperty(name);
}
var $stats = $wnd.__gwtStatsEvent ? function(a) {
return $wnd.__gwtStatsEvent && $wnd.__gwtStatsEvent(a);
} : null;
var $sessionId = $wnd.__gwtStatsSessionId ? $wnd.__gwtStatsSessionId : null;
var $intern_0 = 2147483647, $intern_1 = {3:1, 9:1, 8:1}, $intern_2 = {3:1, 6:1, 7:1, 4:1}, $intern_3 = {3:1, 6:1, 4:1}, $intern_4 = {3:1, 17:1}, $intern_5 = {3:1, 14:1, 10:1, 17:1}, $intern_6 = {3:1, 6:1, 7:1, 4:1, 33:1}, $intern_7 = {26:1, 23:1}, $intern_8 = {31:1, 22:1, 29:1, 26:1, 32:1, 23:1, 24:1}, $intern_9 = {31:1, 22:1, 29:1, 26:1, 50:1, 32:1, 23:1, 24:1, 21:1}, $intern_10 = {81:1}, $intern_11 = {3:1, 81:1}, $intern_12 = {48:1, 3:1}, $intern_13 = {3:1, 4:1}, $intern_14 = {3:1}, $intern_15 = 1000, $intern_16 = {22:1}, $intern_17 = {131:1, 3:1, 14:1, 10:1, 17:1}, $intern_18 = 4194303, $intern_19 = 1048575, $intern_20 = 4194304, $intern_21 = 17592186044416, $intern_22 = 524288, $intern_23 = -17592186044416, $intern_24 = {168:1}, $intern_25 = -2147483648, $intern_26 = 1024, $intern_27 = 4096, $intern_28 = 2048, $intern_29 = 32768, $intern_30 = 16384, $intern_31 = 65536, $intern_32 = 131072, $intern_33 = 262144, $intern_34 = 1048576, $intern_35 = 2097152, $intern_36 = 8388608, $intern_37 = 16777216, $intern_38 = 33554432, $intern_39 = 67108864, $intern_40 = {31:1, 22:1, 29:1, 26:1, 50:1, 32:1, 192:1, 23:1, 24:1, 21:1}, $intern_41 = {982:1, 27:1}, $intern_42 = {21:1}, $intern_43 = {298:1}, $intern_44 = {3:1, 14:1, 64:1, 10:1, 17:1}, $intern_45 = {3:1, 9:1, 45:1, 218:1}, $intern_46 = 65535, $intern_47 = {21:1, 49:1}, $intern_48 = 1019, $intern_49 = {21:1, 49:1, 104:1}, $intern_50 = 1016, $intern_51 = {21:1, 49:1, 40:1}, $intern_52 = {3:1, 21:1, 49:1, 40:1, 214:1}, $intern_53 = {3:1, 21:1, 49:1, 104:1}, $intern_54 = {3:1, 9:1, 138:1}, $intern_55 = 1017, $intern_56 = {3:1, 4:1, 374:1};
var _, prototypesByTypeId_0, initFnList_0, permutationId = -1;
function setGwtProperty(propertyName, propertyValue){
  typeof window === 'object' && typeof window['$gwt'] === 'object' && (window['$gwt'][propertyName] = propertyValue);
}

function gwtOnLoad_0(errFn, modName, modBase, softPermutationId){
  ensureModuleInit();
  var initFnList = initFnList_0;
  $moduleName = modName;
  $moduleBase = modBase;
  permutationId = softPermutationId;
  function initializeModules(){
    for (var i = 0; i < initFnList.length; i++) {
      initFnList[i]();
    }
  }

  if (errFn) {
    try {
      $entry(initializeModules)();
    }
     catch (e) {
      errFn(modName, e);
    }
  }
   else {
    $entry(initializeModules)();
  }
}

function ensureModuleInit(){
  initFnList_0 == null && (initFnList_0 = []);
}

function addInitFunctions(){
  ensureModuleInit();
  var initFnList = initFnList_0;
  for (var i = 0; i < arguments.length; i++) {
    initFnList.push(arguments[i]);
  }
}

function typeMarkerFn(){
}

function toString_36(object){
  var number;
  if (Array.isArray(object) && object.typeMarker === typeMarkerFn) {
    return $getName(getClass__Ljava_lang_Class___devirtual$(object)) + '@' + (number = hashCode__I__devirtual$(object) >>> 0 , number.toString(16));
  }
  return object.toString();
}

function portableObjCreate(obj){
  function F(){
  }

  ;
  F.prototype = obj || {};
  return new F;
}

function emptyMethod(){
}

function defineClass(typeId, superTypeIdOrPrototype, castableTypeMap){
  var prototypesByTypeId = prototypesByTypeId_0, superPrototype;
  var prototype_0 = prototypesByTypeId[typeId];
  var clazz = prototype_0 instanceof Array?prototype_0[0]:null;
  if (prototype_0 && !clazz) {
    _ = prototype_0;
  }
   else {
    _ = (superPrototype = superTypeIdOrPrototype && superTypeIdOrPrototype.prototype , !superPrototype && (superPrototype = prototypesByTypeId_0[superTypeIdOrPrototype]) , portableObjCreate(superPrototype));
    _.castableTypeMap = castableTypeMap;
    !superTypeIdOrPrototype && (_.typeMarker = typeMarkerFn);
    prototypesByTypeId[typeId] = _;
  }
  for (var i = 3; i < arguments.length; ++i) {
    arguments[i].prototype = _;
  }
  clazz && (_.___clazz = clazz);
}

function bootstrap(){
  prototypesByTypeId_0 = {};
  !Array.isArray && (Array.isArray = function(vArg){
    return Object.prototype.toString.call(vArg) === '[object Array]';
  }
  );
  function now_0(){
    return (new Date).getTime();
  }

  !Date.now && (Date.now = now_0);
}

bootstrap();
function Object_0(){
}

function equals_Ljava_lang_Object__Z__devirtual$(this$static, other){
  return instanceOfString(this$static)?$equals_6(this$static, other):instanceOfDouble(this$static)?(checkCriticalNotNull(this$static) , this$static === other):instanceOfBoolean(this$static)?(checkCriticalNotNull(this$static) , this$static === other):hasJavaObjectVirtualDispatch(this$static)?this$static.equals_0(other):isJavaArray(this$static)?this$static === other:!!this$static && !!this$static.equals?this$static.equals(other):maskUndefined(this$static) === maskUndefined(other);
}

function getClass__Ljava_lang_Class___devirtual$(this$static){
  return instanceOfString(this$static)?Ljava_lang_String_2_classLit:instanceOfDouble(this$static)?Ljava_lang_Double_2_classLit:instanceOfBoolean(this$static)?Ljava_lang_Boolean_2_classLit:hasJavaObjectVirtualDispatch(this$static)?this$static.___clazz:isJavaArray(this$static)?this$static.___clazz:this$static.___clazz || Array.isArray(this$static) && getClassLiteralForArray(Lcom_google_gwt_core_client_JavaScriptObject_2_classLit, 1) || Lcom_google_gwt_core_client_JavaScriptObject_2_classLit;
}

function hashCode__I__devirtual$(this$static){
  return instanceOfString(this$static)?getHashCode_1(this$static):instanceOfDouble(this$static)?$hashCode_3(this$static):instanceOfBoolean(this$static)?(checkCriticalNotNull(this$static) , this$static)?1231:1237:hasJavaObjectVirtualDispatch(this$static)?this$static.hashCode_0():isJavaArray(this$static)?getHashCode_0(this$static):$hashCode_2(this$static);
}

defineClass(1, null, {}, Object_0);
_.equals_0 = function equals(other){
  return this === other;
}
;
_.hashCode_0 = function hashCode_0(){
  return getHashCode_0(this);
}
;
_.toString_0 = function toString_0(){
  var number;
  return $getName(getClass__Ljava_lang_Class___devirtual$(this)) + '@' + (number = hashCode__I__devirtual$(this) >>> 0 , number.toString(16));
}
;
_.equals = function(other){
  return this.equals_0(other);
}
;
_.hashCode = function(){
  return this.hashCode_0();
}
;
_.toString = function(){
  return this.toString_0();
}
;
function canCast(src_0, dstId){
  if (instanceOfString(src_0)) {
    return !!stringCastMap[dstId];
  }
   else if (src_0.castableTypeMap) {
    return !!src_0.castableTypeMap[dstId];
  }
   else if (instanceOfDouble(src_0)) {
    return !!doubleCastMap[dstId];
  }
   else if (instanceOfBoolean(src_0)) {
    return !!booleanCastMap[dstId];
  }
  return false;
}

function castTo(src_0, dstId){
  checkCriticalType(src_0 == null || canCast(src_0, dstId));
  return src_0;
}

function castToJso(src_0){
  checkCriticalType(src_0 == null || isJsObjectOrFunction(src_0) && !(src_0.typeMarker === typeMarkerFn));
  return src_0;
}

function castToString(src_0){
  checkCriticalType(src_0 == null || instanceOfString(src_0));
  return src_0;
}

function hasJavaObjectVirtualDispatch(src_0){
  return !Array.isArray(src_0) && src_0.typeMarker === typeMarkerFn;
}

function instanceOf(src_0, dstId){
  return src_0 != null && canCast(src_0, dstId);
}

function instanceOfBoolean(src_0){
  return typeof src_0 === 'boolean';
}

function instanceOfDouble(src_0){
  return typeof src_0 === 'number';
}

function instanceOfJso(src_0){
  return src_0 != null && isJsObjectOrFunction(src_0) && !(src_0.typeMarker === typeMarkerFn);
}

function instanceOfNative(src_0, jsType){
  return src_0 && jsType && src_0 instanceof jsType;
}

function instanceOfString(src_0){
  return typeof src_0 === 'string';
}

function isJsObjectOrFunction(src_0){
  return typeof src_0 === 'object' || typeof src_0 === 'function';
}

function maskUndefined(src_0){
  return src_0 == null?null:src_0;
}

function round_int(x_0){
  return Math.max(Math.min(x_0, $intern_0), -2147483648) | 0;
}

function throwClassCastExceptionUnlessNull(o){
  checkCriticalType(o == null);
  return o;
}

var booleanCastMap, doubleCastMap, stringCastMap;
function $ensureNamesAreInitialized(this$static){
  if (this$static.typeName != null) {
    return;
  }
  initializeNames(this$static);
}

function $getName(this$static){
  $ensureNamesAreInitialized(this$static);
  return this$static.typeName;
}

function Class_0(){
  ++nextSequentialId;
  this.typeName = null;
  this.simpleName = null;
  this.packageName = null;
  this.compoundName = null;
  this.canonicalName = null;
  this.typeId = null;
  this.arrayLiterals = null;
}

function createClassObject(packageName, compoundClassName){
  var clazz;
  clazz = new Class_0;
  clazz.packageName = packageName;
  clazz.compoundName = compoundClassName;
  return clazz;
}

function createForClass(packageName, compoundClassName, typeId){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  maybeSetClassLiteral(typeId, clazz);
  return clazz;
}

function createForEnum(packageName, compoundClassName, typeId, enumConstantsFunc){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  maybeSetClassLiteral(typeId, clazz);
  clazz.modifiers = enumConstantsFunc?8:0;
  return clazz;
}

function createForInterface(packageName, compoundClassName){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  clazz.modifiers = 2;
  return clazz;
}

function createForPrimitive(className, primitiveTypeId){
  var clazz;
  clazz = createClassObject('', className);
  clazz.typeId = primitiveTypeId;
  clazz.modifiers = 1;
  return clazz;
}

function getClassLiteralForArray_0(leafClass, dimensions){
  var arrayLiterals = leafClass.arrayLiterals = leafClass.arrayLiterals || [];
  return arrayLiterals[dimensions] || (arrayLiterals[dimensions] = leafClass.createClassLiteralForArray(dimensions));
}

function getPrototypeForClass(clazz){
  if (clazz.isPrimitive()) {
    return null;
  }
  var typeId = clazz.typeId;
  return prototypesByTypeId_0[typeId];
}

function initializeNames(clazz){
  if (clazz.isArray_0()) {
    var componentType = clazz.componentType;
    componentType.isPrimitive()?(clazz.typeName = '[' + componentType.typeId):!componentType.isArray_0()?(clazz.typeName = '[L' + componentType.getName() + ';'):(clazz.typeName = '[' + componentType.getName());
    clazz.canonicalName = componentType.getCanonicalName() + '[]';
    clazz.simpleName = componentType.getSimpleName() + '[]';
    return;
  }
  var packageName = clazz.packageName;
  var compoundName = clazz.compoundName;
  compoundName = compoundName.split('/');
  clazz.typeName = join_0('.', [packageName, join_0('$', compoundName)]);
  clazz.canonicalName = join_0('.', [packageName, join_0('.', compoundName)]);
  clazz.simpleName = compoundName[compoundName.length - 1];
}

function join_0(separator, strings){
  var i = 0;
  while (!strings[i] || strings[i] == '') {
    i++;
  }
  var result = strings[i++];
  for (; i < strings.length; i++) {
    if (!strings[i] || strings[i] == '') {
      continue;
    }
    result += separator + strings[i];
  }
  return result;
}

function maybeSetClassLiteral(typeId, clazz){
  var proto;
  if (!typeId) {
    return;
  }
  clazz.typeId = typeId;
  var prototype_0 = getPrototypeForClass(clazz);
  if (!prototype_0) {
    prototypesByTypeId_0[typeId] = [clazz];
    return;
  }
  prototype_0.___clazz = clazz;
}

defineClass(306, 1, {}, Class_0);
_.createClassLiteralForArray = function createClassLiteralForArray(dimensions){
  var clazz;
  clazz = new Class_0;
  clazz.modifiers = 4;
  dimensions > 1?(clazz.componentType = getClassLiteralForArray_0(this, dimensions - 1)):(clazz.componentType = this);
  return clazz;
}
;
_.getCanonicalName = function getCanonicalName(){
  $ensureNamesAreInitialized(this);
  return this.canonicalName;
}
;
_.getName = function getName_16(){
  return $getName(this);
}
;
_.getSimpleName = function getSimpleName(){
  $ensureNamesAreInitialized(this);
  return this.simpleName;
}
;
_.isArray_0 = function isArray(){
  return (this.modifiers & 4) != 0;
}
;
_.isPrimitive = function isPrimitive(){
  return (this.modifiers & 1) != 0;
}
;
_.toString_0 = function toString_47(){
  return ((this.modifiers & 2) != 0?'interface ':(this.modifiers & 1) != 0?'':'class ') + ($ensureNamesAreInitialized(this) , this.typeName);
}
;
_.modifiers = 0;
var nextSequentialId = 1;
var Ljava_lang_Object_2_classLit = createForClass('java.lang', 'Object', 1);
var Ljava_lang_Class_2_classLit = createForClass('java.lang', 'Class', 306);
function $clinit_SharedConfig(){
  $clinit_SharedConfig = emptyMethod;
  instance_0 = new SharedConfig;
}

function $setDev_mode(this$static, dev_mode){
  this$static.dev_mode = dev_mode;
}

function SharedConfig(){
}

defineClass(421, 1, {}, SharedConfig);
_.dev_mode = false;
_.show_stickingpolygon = true;
_.stickingEnabled = true;
var instance_0;
var Lcom_baselet_control_config_SharedConfig_2_classLit = createForClass('com.baselet.control.config', 'SharedConfig', 421);
function $toString(this$static){
  return this$static.name_0 != null?this$static.name_0:'' + this$static.ordinal;
}

function Enum(name_0, ordinal){
  this.name_0 = name_0;
  this.ordinal = ordinal;
}

defineClass(8, 1, $intern_1);
_.equals_0 = function equals_6(other){
  return this === other;
}
;
_.hashCode_0 = function hashCode_7(){
  return getHashCode_0(this);
}
;
_.toString_0 = function toString_11(){
  return $toString(this);
}
;
_.ordinal = 0;
var Ljava_lang_Enum_2_classLit = createForClass('java.lang', 'Enum', 8);
function $clinit_Program(){
  $clinit_Program = emptyMethod;
  log_2 = getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_control_enums_Program_2_classLit) , Lcom_baselet_control_enums_Program_2_classLit.typeName));
}

function Program(runtimeType){
  $info(log_2, 'Initializing Program: Version=14.4.0-SNAPSHOT, Runtime=' + runtimeType);
  this.programName = 'UMLet';
  this.extension = 'uxf';
  'http://www.' + this.programName.toLowerCase() + '.com';
  runtimeType == ($clinit_RuntimeType() , ECLIPSE_PLUGIN)?this.programName.toLowerCase() + 'plugin.cfg':this.programName.toLowerCase() + '.cfg';
}

function init(runtimeType){
  $clinit_Program();
  instance_1 = new Program(runtimeType);
}

defineClass(420, 1, {}, Program);
var instance_1, log_2;
var Lcom_baselet_control_enums_Program_2_classLit = createForClass('com.baselet.control.enums', 'Program', 420);
function $clinit_RuntimeType(){
  $clinit_RuntimeType = emptyMethod;
  STANDALONE = new RuntimeType('STANDALONE', 0);
  ECLIPSE_PLUGIN = new RuntimeType('ECLIPSE_PLUGIN', 1);
  BATCH = new RuntimeType('BATCH', 2);
  GWT = new RuntimeType('GWT', 3);
}

function RuntimeType(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_8(){
  $clinit_RuntimeType();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_control_enums_RuntimeType_2_classLit, 1), $intern_2, 169, 0, [STANDALONE, ECLIPSE_PLUGIN, BATCH, GWT]);
}

defineClass(169, 8, {169:1, 3:1, 9:1, 8:1}, RuntimeType);
var BATCH, ECLIPSE_PLUGIN, GWT, STANDALONE;
var Lcom_baselet_control_enums_RuntimeType_2_classLit = createForEnum('com.baselet.control.enums', 'RuntimeType', 169, values_8);
function $$init(this$static){
  this$static.stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_3, 121, 0, 0, 1);
}

function $addSuppressed(this$static, exception){
  checkCriticalNotNull_0(exception, 'Cannot suppress a null exception.');
  checkCriticalArgument(exception != this$static, 'Exception can not suppress itself.');
  if (this$static.disableSuppression) {
    return;
  }
  this$static.suppressedExceptions == null?(this$static.suppressedExceptions = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Throwable_2_classLit, 1), $intern_3, 17, 0, [exception])):(this$static.suppressedExceptions[this$static.suppressedExceptions.length] = exception);
}

function $fillInStackTrace(this$static){
  if (this$static.writetableStackTrace) {
    this$static.backingJsObject !== '__noinit__' && this$static.initializeBackingError();
    this$static.stackTrace = null;
  }
  return this$static;
}

function $printStackTraceImpl(this$static, out, prefix, ident){
  var t, t$array, t$index, t$max, theCause;
  out.println(ident + prefix + this$static);
  $printStackTraceItems(this$static, out, ident);
  for (t$array = (this$static.suppressedExceptions == null && (this$static.suppressedExceptions = initUnidimensionalArray(Ljava_lang_Throwable_2_classLit, $intern_3, 17, 0, 0, 1)) , this$static.suppressedExceptions) , t$index = 0 , t$max = t$array.length; t$index < t$max; ++t$index) {
    t = t$array[t$index];
    $printStackTraceImpl(t, out, 'Suppressed: ', '\t' + ident);
  }
  theCause = this$static.cause;
  !!theCause && $printStackTraceImpl(theCause, out, 'Caused by: ', ident);
}

function $printStackTraceItems(this$static, out, ident){
  var element, element$array, element$index, element$max, stackTrace;
  for (element$array = (this$static.stackTrace == null && (this$static.stackTrace = ($clinit_StackTraceCreator() , stackTrace = collector.getStackTrace(this$static) , dropInternalFrames(stackTrace))) , this$static.stackTrace) , element$index = 0 , element$max = element$array.length; element$index < element$max; ++element$index) {
    element = element$array[element$index];
    out.println(ident + '\tat ' + element);
  }
}

function $setBackingJsObject(this$static, backingJsObject){
  this$static.backingJsObject = backingJsObject;
  backingJsObject != null && setPropertySafe(backingJsObject, '__java$exception', this$static);
}

function $toString_0(this$static, message){
  var className;
  className = $getName(this$static.___clazz);
  return message == null?className:className + ': ' + message;
}

function Throwable(message, cause){
  $$init(this);
  this.cause = cause;
  this.detailMessage = message;
  $fillInStackTrace(this);
  this.initializeBackingError();
}

function fixIE(e){
  if (!('stack' in e)) {
    try {
      throw e;
    }
     catch (ignored) {
    }
  }
  return e;
}

function of(e){
  var throwable;
  if (e != null) {
    throwable = e['__java$exception'];
    if (throwable) {
      return throwable;
    }
  }
  return instanceOfNative(e, TypeError)?new NullPointerException_0(e):new JsException(e);
}

defineClass(17, 1, $intern_4);
_.createError = function createError(msg){
  return new Error(msg);
}
;
_.getMessage = function getMessage(){
  return this.detailMessage;
}
;
_.initializeBackingError = function initializeBackingError(){
  var className, errorMessage, message;
  message = this.detailMessage == null?null:this.detailMessage.replace(new RegExp('\n', 'g'), ' ');
  errorMessage = (className = $getName(this.___clazz) , message == null?className:className + ': ' + message);
  $setBackingJsObject(this, fixIE(this.createError(errorMessage)));
  captureStackTrace(this);
}
;
_.toString_0 = function toString_14(){
  return $toString_0(this, this.getMessage());
}
;
_.backingJsObject = '__noinit__';
_.disableSuppression = false;
_.writetableStackTrace = true;
var Ljava_lang_Throwable_2_classLit = createForClass('java.lang', 'Throwable', 17);
function Exception_0(){
  $$init(this);
  $fillInStackTrace(this);
  this.initializeBackingError();
}

function Exception_1(message){
  $$init(this);
  this.detailMessage = message;
  $fillInStackTrace(this);
  this.initializeBackingError();
}

defineClass(14, 17, {3:1, 14:1, 17:1});
var Ljava_lang_Exception_2_classLit = createForClass('java.lang', 'Exception', 14);
function RuntimeException(){
  Exception_0.call(this);
}

function RuntimeException_0(message){
  Exception_1.call(this, message);
}

function RuntimeException_1(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(10, 14, $intern_5, RuntimeException, RuntimeException_0, RuntimeException_1);
var Ljava_lang_RuntimeException_2_classLit = createForClass('java.lang', 'RuntimeException', 10);
function $onModuleLoad(this$static){
  var s, i;
  $info(this$static.log_0, 'Starting GUI ...');
  init(($clinit_RuntimeType() , GWT));
  $setDev_mode(($clinit_SharedConfig() , $clinit_SharedConfig() , instance_0), getParameter('dev') != null);
  $clinit_BrowserStorage();
  (GetVersion() == 1?(localStorage_0 = null , true):initLocalStorageAndCheckIfAvailable()) || (get_1() == ($clinit_Browser() , INTERNET_EXPLORER) && $startsWith(($clinit_Impl() , s = $doc.location.href , i = s.indexOf('#') , i != -1 && (s = s.substring(0, i)) , i = s.indexOf('?') , i != -1 && (s = s.substring(0, i)) , i = s.lastIndexOf('/') , i != -1 && (s = s.substring(0, i)) , s.length > 0?s + '/':''), 'file:')?showFeatureNotSupported('You have opened this webpage from your filesystem, therefore<br/>Internet Explorer will not support local storage<br/><br/>Please use another browser like Firefox or Chrome,<br/>or open this application using the web url', false):showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'local storage' (or has cookies disabled)<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false));
  if (typeof FileReader != 'undefined') {
    showInfo('Loading application ... please wait ...');
    runAsync(1, new BaseletGWT$1);
    (null , instance_0).dev_mode || addWindowClosingHandler(new BaseletGWT$2);
  }
   else {
    showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'file reader'<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false);
  }
  $info(this$static.log_0, 'GUI started');
}

function BaseletGWT(){
  this.log_0 = getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_gwt_client_BaseletGWT_2_classLit) , Lcom_baselet_gwt_client_BaseletGWT_2_classLit.typeName));
}

defineClass(385, 1, {}, BaseletGWT);
var Lcom_baselet_gwt_client_BaseletGWT_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT', 385);
var Lcom_google_gwt_core_client_RunAsyncCallback_2_classLit = createForInterface('com.google.gwt.core.client', 'RunAsyncCallback');
function BaseletGWT$1(){
}

defineClass(392, 1, {299:1}, BaseletGWT$1);
var Lcom_baselet_gwt_client_BaseletGWT$1_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT/1', 392);
function BaseletGWT$2(){
}

defineClass(393, 1, {27:1, 997:1}, BaseletGWT$2);
var Lcom_baselet_gwt_client_BaseletGWT$2_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT/2', 393);
function $clinit_Browser(){
  $clinit_Browser = emptyMethod;
  INTERNET_EXPLORER = new Browser('INTERNET_EXPLORER', 0, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['MSIE']));
  FIREFOX = new Browser('FIREFOX', 1, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Firefox']));
  CHROME = new Browser('CHROME', 2, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Chrome']));
  OPERA = new Browser('OPERA', 3, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Opera']));
  ANDROID_STOCK_BROWSER = new Browser('ANDROID_STOCK_BROWSER', 4, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Android']));
  UNKNOWN = new Browser('UNKNOWN', 5, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['######']));
}

function Browser(enum$name, enum$ordinal, browserFilters){
  Enum.call(this, enum$name, enum$ordinal);
  this.browserFilters = browserFilters;
}

function browserFiltersMatch(currentAgent, b){
  var filterString, filterString$array, filterString$index, filterString$max;
  for (filterString$array = b.browserFilters , filterString$index = 0 , filterString$max = filterString$array.length; filterString$index < filterString$max; ++filterString$index) {
    filterString = filterString$array[filterString$index];
    if ($indexOf_0(currentAgent, (checkCriticalNotNull(filterString) , filterString)) == -1) {
      return false;
    }
  }
  return true;
}

function get_1(){
  $clinit_Browser();
  var b, b$array, b$index, b$max, currentAgent;
  currentAgent = navigator.userAgent;
  for (b$array = stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_gwt_client_base_Browser_2_classLit, 1), $intern_2, 123, 0, [INTERNET_EXPLORER, FIREFOX, CHROME, OPERA, ANDROID_STOCK_BROWSER, UNKNOWN]) , b$index = 0 , b$max = b$array.length; b$index < b$max; ++b$index) {
    b = b$array[b$index];
    if (browserFiltersMatch(currentAgent, b)) {
      return b;
    }
  }
  return UNKNOWN;
}

function values_23(){
  $clinit_Browser();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_gwt_client_base_Browser_2_classLit, 1), $intern_2, 123, 0, [INTERNET_EXPLORER, FIREFOX, CHROME, OPERA, ANDROID_STOCK_BROWSER, UNKNOWN]);
}

defineClass(123, 8, {123:1, 3:1, 9:1, 8:1}, Browser);
var ANDROID_STOCK_BROWSER, CHROME, FIREFOX, INTERNET_EXPLORER, OPERA, UNKNOWN;
var Lcom_baselet_gwt_client_base_Browser_2_classLit = createForEnum('com.baselet.gwt.client.base', 'Browser', 123, values_23);
function $clinit_Notification(){
  $clinit_Notification = emptyMethod;
  element_0 = $getElement(get_6('featurewarning'));
}

function showFeatureNotSupported(text_0, fadeOut){
  $clinit_Notification();
  if ($equals_6(text_0, lastShownFeatureNotSupportedText)) {
    return;
  }
  lastShownFeatureNotSupportedText = text_0;
  element_0.style['color'] = 'red';
  $setInnerHTML(element_0, text_0);
  fadeOut && fade_0(element_0, 7000, 3000);
}

function showInfo(text_0){
  $clinit_Notification();
  element_0.style['color'] = 'blue';
  $setInnerHTML(element_0, text_0);
  fade_0(element_0, 4000, 2000);
}

var element_0, lastShownFeatureNotSupportedText;
function fade(element, startOpacity, endOpacity, totalTimeMillis){
  var deltaOpacity, stepLengthMillis;
  stepLengthMillis = totalTimeMillis / 30 | 0;
  stepCount = 0;
  deltaOpacity = (endOpacity - startOpacity) / 30;
  timerFader = new Notification$ElementFader$2(startOpacity, deltaOpacity, element, endOpacity);
  $scheduleRepeating(timerFader, stepLengthMillis);
}

function fade_0(element, delay, totalTimeMillis){
  !!timer_0 && $cancel(timer_0);
  !!timerFader && $cancel(timerFader);
  $clinit_DOM();
  element.style['opacity'] = '1.0';
  timer_0 = new Notification$ElementFader$1(element, totalTimeMillis);
  $schedule(timer_0, delay);
}

var stepCount = 0, timer_0, timerFader;
function $cancel(this$static){
  if (!this$static.timerId) {
    return;
  }
  ++this$static.cancelCounter;
  this$static.isRepeating?clearInterval_0(this$static.timerId.value_0):clearTimeout_0(this$static.timerId.value_0);
  this$static.timerId = null;
}

function $schedule(this$static, delayMillis){
  if (delayMillis < 0) {
    throw toJs(new IllegalArgumentException_0('must be non-negative'));
  }
  !!this$static.timerId && $cancel(this$static);
  this$static.isRepeating = false;
  this$static.timerId = valueOf_9(setTimeout_0(createCallback(this$static, this$static.cancelCounter), delayMillis));
}

function $scheduleRepeating(this$static, periodMillis){
  if (periodMillis <= 0) {
    throw toJs(new IllegalArgumentException_0('must be positive'));
  }
  !!this$static.timerId && $cancel(this$static);
  this$static.isRepeating = true;
  this$static.timerId = valueOf_9(setInterval_0(createCallback(this$static, this$static.cancelCounter), periodMillis));
}

function Timer_0(){
}

function clearInterval_0(timerId){
  $wnd.clearInterval(timerId);
}

function clearTimeout_0(timerId){
  $wnd.clearTimeout(timerId);
}

function createCallback(timer, cancelCounter){
  return $entry(function(){
    timer.fire(cancelCounter);
  }
  );
}

function setInterval_0(func, time){
  return $wnd.setInterval(func, time);
}

function setTimeout_0(func, time){
  return $wnd.setTimeout(func, time);
}

defineClass(170, 1, {});
_.fire = function fire(scheduleCancelCounter){
  if (scheduleCancelCounter != this.cancelCounter) {
    return;
  }
  this.isRepeating || (this.timerId = null);
  this.run();
}
;
_.cancelCounter = 0;
_.isRepeating = false;
_.timerId = null;
var Lcom_google_gwt_user_client_Timer_2_classLit = createForClass('com.google.gwt.user.client', 'Timer', 170);
function Notification$ElementFader$1(val$element, val$totalTimeMillis){
  this.val$element1 = val$element;
  this.val$startOpacity2 = 1;
  this.val$endOpacity3 = 0;
  this.val$totalTimeMillis4 = val$totalTimeMillis;
  Timer_0.call(this);
}

defineClass(422, 170, {}, Notification$ElementFader$1);
_.run = function run_11(){
  fade(this.val$element1, this.val$startOpacity2, this.val$endOpacity3, this.val$totalTimeMillis4);
}
;
_.val$endOpacity3 = 0;
_.val$startOpacity2 = 0;
_.val$totalTimeMillis4 = 0;
var Lcom_baselet_gwt_client_base_Notification$ElementFader$1_2_classLit = createForClass('com.baselet.gwt.client.base', 'Notification/ElementFader/1', 422);
function Notification$ElementFader$2(val$startOpacity, val$deltaOpacity, val$element, val$endOpacity){
  this.val$startOpacity1 = val$startOpacity;
  this.val$deltaOpacity2 = val$deltaOpacity;
  this.val$element3 = val$element;
  this.val$endOpacity4 = val$endOpacity;
  Timer_0.call(this);
}

defineClass(423, 170, {}, Notification$ElementFader$2);
_.run = function run_12(){
  var opacity;
  opacity = this.val$startOpacity1 + stepCount * this.val$deltaOpacity2;
  setStyleAttribute(this.val$element3, '' + opacity);
  ++stepCount;
  if (stepCount == 30) {
    setStyleAttribute(this.val$element3, '' + this.val$endOpacity4);
    $cancel(this);
  }
}
;
_.val$deltaOpacity2 = 0;
_.val$endOpacity4 = 0;
_.val$startOpacity1 = 0;
var Lcom_baselet_gwt_client_base_Notification$ElementFader$2_2_classLit = createForClass('com.baselet.gwt.client.base', 'Notification/ElementFader/2', 423);
function $clinit_BrowserStorage(){
  $clinit_BrowserStorage = emptyMethod;
  getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_gwt_client_BaseletGWT_2_classLit) , Lcom_baselet_gwt_client_BaseletGWT_2_classLit.typeName));
}

function initLocalStorageAndCheckIfAvailable(){
  $clinit_BrowserStorage();
  try {
    localStorage_0 = (!localStorage_1 && ($clinit_Storage$StorageSupportDetector() , localStorageSupported) && (localStorage_1 = new Storage_0) , localStorage_1);
    return !!localStorage_0;
  }
   catch ($e0) {
    $e0 = toJava($e0);
    if (instanceOf($e0, 14)) {
      return false;
    }
     else 
      throw toJs($e0);
  }
}

var localStorage_0;
function $getElement(this$static){
  return $clinit_DOM() , this$static.element;
}

function $setElement_0(this$static, elem){
  this$static.element = elem;
}

defineClass(23, 1, $intern_7);
_.getElement = function getElement(){
  return $getElement(this);
}
;
_.toString_0 = function toString_31(){
  if (!this.element) {
    return '(null handle)';
  }
  return this.getElement().outerHTML;
}
;
var Lcom_google_gwt_user_client_ui_UIObject_2_classLit = createForClass('com.google.gwt.user.client.ui', 'UIObject', 23);
function $onAttach(this$static){
  var bitsToAdd;
  if (this$static.isAttached()) {
    throw toJs(new IllegalStateException_0("Should only call onAttach when the widget is detached from the browser's document"));
  }
  this$static.attached = true;
  $clinit_DOM();
  setEventListener(this$static.getElement(), this$static);
  bitsToAdd = this$static.eventsToSink;
  this$static.eventsToSink = -1;
  bitsToAdd > 0 && (this$static.eventsToSink == -1?sinkEvents(this$static.getElement(), bitsToAdd | (this$static.getElement().__eventBits || 0)):(this$static.eventsToSink |= bitsToAdd));
  this$static.doAttachChildren();
  this$static.onLoad();
  fire_0(this$static, true);
}

function $onBrowserEvent(this$static, event_0){
  var related;
  switch ($clinit_DOM() , $eventGetTypeInt(event_0.type)) {
    case 16:
    case 32:
      related = event_0.relatedTarget;
      if (!!related && isOrHasChildImpl(this$static.getElement(), related)) {
        return;
      }

  }
  fireNativeEvent(event_0, this$static, this$static.getElement());
}

function $onDetach(this$static){
  if (!this$static.isAttached()) {
    throw toJs(new IllegalStateException_0("Should only call onDetach when the widget is attached to the browser's document"));
  }
  try {
    this$static.onUnload();
    fire_0(this$static, false);
  }
   finally {
    try {
      this$static.doDetachChildren();
    }
     finally {
      $clinit_DOM();
      this$static.getElement().__listener = null;
      this$static.attached = false;
    }
  }
}

function $setParent(this$static, parent_0){
  var oldParent;
  oldParent = this$static.parent_0;
  if (!parent_0) {
    try {
      !!oldParent && oldParent.isAttached() && this$static.onDetach();
    }
     finally {
      this$static.parent_0 = null;
    }
  }
   else {
    if (oldParent) {
      throw toJs(new IllegalStateException_0('Cannot set a new parent without first clearing the old parent'));
    }
    this$static.parent_0 = parent_0;
    parent_0.isAttached() && this$static.onAttach();
  }
}

defineClass(24, 23, $intern_8);
_.doAttachChildren = function doAttachChildren(){
}
;
_.doDetachChildren = function doDetachChildren(){
}
;
_.fireEvent = function fireEvent(event_0){
  !!this.handlerManager && $fireEvent(this.handlerManager, event_0);
}
;
_.isAttached = function isAttached(){
  return this.attached;
}
;
_.onAttach = function onAttach(){
  $onAttach(this);
}
;
_.onBrowserEvent = function onBrowserEvent(event_0){
  $onBrowserEvent(this, event_0);
}
;
_.onDetach = function onDetach(){
  $onDetach(this);
}
;
_.onLoad = function onLoad(){
}
;
_.onUnload = function onUnload(){
}
;
_.attached = false;
_.eventsToSink = 0;
var Lcom_google_gwt_user_client_ui_Widget_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Widget', 24);
defineClass(1014, 24, $intern_9);
_.doAttachChildren = function doAttachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , attachCommand));
}
;
_.doDetachChildren = function doDetachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , detachCommand));
}
;
var Lcom_google_gwt_user_client_ui_Panel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Panel', 1014);
function $remove_0(this$static, w){
  var elem;
  if (w.parent_0 != this$static) {
    return false;
  }
  try {
    $setParent(w, null);
  }
   finally {
    elem = w.getElement();
    $removeChild(($clinit_DOM() , $clinit_DOM() , $getParentElement(elem)), elem);
    $remove_8(this$static.children, w);
  }
  return true;
}

function ComplexPanel(){
  this.children = new WidgetCollection(this);
}

defineClass(191, 1014, $intern_9);
_.iterator = function iterator_2(){
  return new WidgetCollection$WidgetIterator(this.children);
}
;
_.remove_1 = function remove_4(w){
  return $remove_0(this, w);
}
;
var Lcom_google_gwt_user_client_ui_ComplexPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'ComplexPanel', 191);
function GetVersion(){
  if (isVsCodeVersion()) {
    return 1;
  }
  return 0;
}

function isVsCodeVersion(){
  if (typeof window.parent.vscode !== 'undefined') {
    return true;
  }
  return false;
}

defineClass(999, 1, {});
_.toString_0 = function toString_32(){
  return 'An event type';
}
;
var Lcom_google_web_bindery_event_shared_Event_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event', 999);
function $overrideSource(this$static, source){
  this$static.source = source;
}

defineClass(998, 999, {});
_.revive = function revive(){
  this.dead = false;
  this.source = null;
}
;
_.dead = false;
var Lcom_google_gwt_event_shared_GwtEvent_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent', 998);
function fireNativeEvent(nativeEvent, handlerSource, relativeElem){
  var currentNative, currentRelativeElem, type_0, type$iterator, types;
  if (registered) {
    types = castTo(registered.unsafeGet(nativeEvent.type), 40);
    if (types) {
      for (type$iterator = types.iterator(); type$iterator.hasNext_0();) {
        type_0 = castTo(type$iterator.next_1(), 53);
        currentNative = type_0.flyweight.nativeEvent;
        currentRelativeElem = type_0.flyweight.relativeElem;
        type_0.flyweight.setNativeEvent(nativeEvent);
        type_0.flyweight.setRelativeElement(relativeElem);
        handlerSource.fireEvent(type_0.flyweight);
        type_0.flyweight.setNativeEvent(currentNative);
        type_0.flyweight.setRelativeElement(currentRelativeElem);
      }
    }
  }
}

var registered;
function $containsEntry(this$static, entry){
  var key, ourValue, value_0;
  key = entry.getKey();
  value_0 = entry.getValue_0();
  ourValue = this$static.get_1(key);
  if (!(maskUndefined(value_0) === maskUndefined(ourValue) || value_0 != null && equals_Ljava_lang_Object__Z__devirtual$(value_0, ourValue))) {
    return false;
  }
  if (ourValue == null && !this$static.containsKey(key)) {
    return false;
  }
  return true;
}

function $implFindEntry(this$static, key, remove){
  var entry, iter, k;
  for (iter = this$static.entrySet_0().iterator(); iter.hasNext_0();) {
    entry = castTo(iter.next_1(), 44);
    k = entry.getKey();
    if (maskUndefined(key) === maskUndefined(k) || key != null && equals_Ljava_lang_Object__Z__devirtual$(key, k)) {
      if (remove) {
        entry = new AbstractMap$SimpleEntry(entry.getKey(), entry.getValue_0());
        iter.remove_3();
      }
      return entry;
    }
  }
  return null;
}

function $toString_4(this$static, o){
  return o === this$static?'(this Map)':o == null?'null':toString_36(o);
}

function getEntryValueOrNull(entry){
  return !entry?null:entry.getValue_0();
}

defineClass(1018, 1, $intern_10);
_.containsEntry = function containsEntry(entry){
  return $containsEntry(this, entry);
}
;
_.containsKey = function containsKey(key){
  return !!$implFindEntry(this, key, false);
}
;
_.equals_0 = function equals_17(obj){
  var entry, entry$iterator, otherMap;
  if (obj === this) {
    return true;
  }
  if (!instanceOf(obj, 81)) {
    return false;
  }
  otherMap = castTo(obj, 81);
  if (this.size_1() != otherMap.size_1()) {
    return false;
  }
  for (entry$iterator = otherMap.entrySet_0().iterator(); entry$iterator.hasNext_0();) {
    entry = castTo(entry$iterator.next_1(), 44);
    if (!this.containsEntry(entry)) {
      return false;
    }
  }
  return true;
}
;
_.get_1 = function get_2(key){
  return getEntryValueOrNull($implFindEntry(this, key, false));
}
;
_.hashCode_0 = function hashCode_14(){
  return hashCode_29(this.entrySet_0());
}
;
_.put = function put(key, value_0){
  throw toJs(new UnsupportedOperationException_0('Put not supported on this map'));
}
;
_.size_1 = function size_2(){
  return this.entrySet_0().size_1();
}
;
_.toString_0 = function toString_33(){
  var entry, entry$iterator, joiner;
  joiner = new StringJoiner('{', '}');
  for (entry$iterator = this.entrySet_0().iterator(); entry$iterator.hasNext_0();) {
    entry = castTo(entry$iterator.next_1(), 44);
    $add_17(joiner, $toString_4(this, entry.getKey()) + '=' + $toString_4(this, entry.getValue_0()));
  }
  return !joiner.builder?joiner.emptyValue:joiner.suffix.length == 0?joiner.builder.string:joiner.builder.string + ('' + joiner.suffix);
}
;
var Ljava_util_AbstractMap_2_classLit = createForClass('java.util', 'AbstractMap', 1018);
function $containsKey_0(this$static, key){
  return instanceOfString(key)?$hasStringValue(this$static, key):!!$getEntry(this$static.hashCodeMap, key);
}

function $get_0(this$static, key){
  return instanceOfString(key)?$getStringValue(this$static, key):getEntryValueOrNull($getEntry(this$static.hashCodeMap, key));
}

function $getStringValue(this$static, key){
  return key == null?getEntryValueOrNull($getEntry(this$static.hashCodeMap, null)):$get_7(this$static.stringMap, key);
}

function $hasStringValue(this$static, key){
  return key == null?!!$getEntry(this$static.hashCodeMap, null):$contains_5(this$static.stringMap, key);
}

function $put(this$static, key, value_0){
  return instanceOfString(key)?$putStringValue(this$static, key, value_0):$put_1(this$static.hashCodeMap, key, value_0);
}

function $putStringValue(this$static, key, value_0){
  return key == null?$put_1(this$static.hashCodeMap, null, value_0):$put_2(this$static.stringMap, key, value_0);
}

function $reset_0(this$static){
  this$static.hashCodeMap = new InternalHashCodeMap(this$static);
  this$static.stringMap = new InternalStringMap(this$static);
  structureChanged(this$static);
}

function $size(this$static){
  return this$static.hashCodeMap.size_0 + this$static.stringMap.size_0;
}

defineClass(221, 1018, $intern_10);
_.containsKey = function containsKey_0(key){
  return $containsKey_0(this, key);
}
;
_.entrySet_0 = function entrySet(){
  return new AbstractHashMap$EntrySet(this);
}
;
_.get_1 = function get_3(key){
  return $get_0(this, key);
}
;
_.put = function put_0(key, value_0){
  return $put(this, key, value_0);
}
;
_.size_1 = function size_3(){
  return $size(this);
}
;
var Ljava_util_AbstractHashMap_2_classLit = createForClass('java.util', 'AbstractHashMap', 221);
function HashMap(){
  $reset_0(this);
}

defineClass(35, 221, $intern_11, HashMap);
_.equals_1 = function equals_18(value1, value2){
  return maskUndefined(value1) === maskUndefined(value2) || value1 != null && equals_Ljava_lang_Object__Z__devirtual$(value1, value2);
}
;
_.getHashCode = function getHashCode(key){
  var hashCode;
  hashCode = hashCode__I__devirtual$(key);
  return hashCode | 0;
}
;
var Ljava_util_HashMap_2_classLit = createForClass('java.util', 'HashMap', 35);
function $hashCode_2(this$static){
  return !!this$static && !!this$static.hashCode?this$static.hashCode():getHashCode_0(this$static);
}

var Lcom_google_gwt_core_client_JavaScriptObject_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptObject$', 0);
function CodeDownloadException(message){
  RuntimeException_0.call(this, message);
}

defineClass(1034, 10, $intern_5, CodeDownloadException);
var Lcom_google_gwt_core_client_CodeDownloadException_2_classLit = createForClass('com.google.gwt.core.client', 'CodeDownloadException', 1034);
function Duration(){
  this.start_0 = now_1();
}

defineClass(254, 1, {}, Duration);
_.start_0 = 0;
var Lcom_google_gwt_core_client_Duration_2_classLit = createForClass('com.google.gwt.core.client', 'Duration', 254);
function setUncaughtExceptionHandler(handler){
  uncaughtExceptionHandler = handler;
  maybeInitializeWindowOnError();
}

var uncaughtExceptionHandler = null;
function JsException(backingJsObject){
  $$init(this);
  $fillInStackTrace(this);
  this.backingJsObject = backingJsObject;
  backingJsObject != null && setPropertySafe(backingJsObject, '__java$exception', this);
  this.detailMessage = backingJsObject == null?'null':toString_36(backingJsObject);
}

defineClass(189, 10, $intern_5, JsException);
var Ljava_lang_JsException_2_classLit = createForClass('java.lang', 'JsException', 189);
defineClass(415, 189, $intern_5);
var Lcom_google_gwt_core_client_impl_JavaScriptExceptionBase_2_classLit = createForClass('com.google.gwt.core.client.impl', 'JavaScriptExceptionBase', 415);
function $clinit_JavaScriptException(){
  $clinit_JavaScriptException = emptyMethod;
  NOT_SET = new Object_0;
}

function $ensureInit(this$static){
  var exception;
  if (this$static.message_0 == null) {
    exception = maskUndefined(this$static.e) === maskUndefined(NOT_SET)?null:this$static.e;
    this$static.name_0 = exception == null?'null':instanceOfJso(exception)?getExceptionName0(castToJso(exception)):instanceOfString(exception)?'String':$getName(getClass__Ljava_lang_Class___devirtual$(exception));
    this$static.description = this$static.description + ': ' + (instanceOfJso(exception)?getExceptionDescription0(castToJso(exception)):exception + '');
    this$static.message_0 = '(' + this$static.name_0 + ') ' + this$static.description;
  }
}

function JavaScriptException(e){
  $clinit_JavaScriptException();
  JsException.call(this, e);
  this.description = '';
  this.e = e;
  this.description = '';
}

function getExceptionDescription0(e){
  return e == null?null:e.message;
}

function getExceptionName0(e){
  return e == null?null:e.name;
}

defineClass(99, 415, {99:1, 3:1, 14:1, 10:1, 17:1}, JavaScriptException);
_.getMessage = function getMessage_2(){
  return $ensureInit(this) , this.message_0;
}
;
_.getThrown = function getThrown(){
  return maskUndefined(this.e) === maskUndefined(NOT_SET)?null:this.e;
}
;
var NOT_SET;
var Lcom_google_gwt_core_client_JavaScriptException_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptException', 99);
function now_1(){
  if (Date.now) {
    return Date.now();
  }
  return (new Date).getTime();
}

defineClass(996, 1, {});
var Lcom_google_gwt_core_client_Scheduler_2_classLit = createForClass('com.google.gwt.core.client', 'Scheduler', 996);
function $clinit_ScriptInjector(){
  $clinit_ScriptInjector = emptyMethod;
  TOP_WINDOW = $wnd;
}

function attachListeners(scriptElement, callback, removeTag){
  $clinit_ScriptInjector();
  function clearCallbacks(){
    scriptElement.onerror = scriptElement.onreadystatechange = scriptElement.onload = null;
    removeTag && nativeRemove(scriptElement);
  }

  scriptElement.onload = $entry(function(){
    clearCallbacks();
    callback && callback.onSuccess_0(null);
  }
  );
  scriptElement.onerror = $entry(function(){
    clearCallbacks();
    if (callback) {
      var ex = new CodeDownloadException('onerror() called.');
      callback.onFailure(ex);
    }
  }
  );
  scriptElement.onreadystatechange = $entry(function(){
    /loaded|complete/.test(scriptElement.readyState) && scriptElement.onload();
  }
  );
}

function nativeRemove(scriptElement){
  scriptElement.parentNode.removeChild(scriptElement);
}

function nativeSetSrc(element, url_0){
  $clinit_ScriptInjector();
  element.src = url_0;
}

var TOP_WINDOW;
function $inject(this$static){
  var doc, scriptElement, wnd;
  wnd = !this$static.window_0?($clinit_ScriptInjector() , window):this$static.window_0;
  doc = ($clinit_ScriptInjector() , wnd.document);
  scriptElement = doc.createElement('script');
  (!!this$static.callback || this$static.removeTag) && attachListeners(scriptElement, this$static.callback, this$static.removeTag);
  nativeSetSrc(scriptElement, this$static.scriptUrl);
  (doc.head || doc.getElementsByTagName('head')[0]).appendChild(scriptElement);
  return scriptElement;
}

function $setCallback(this$static, callback){
  this$static.callback = callback;
  return this$static;
}

function $setRemoveTag(this$static){
  this$static.removeTag = true;
  return this$static;
}

function ScriptInjector$FromUrl(scriptUrl){
  this.scriptUrl = scriptUrl;
}

defineClass(262, 1, {}, ScriptInjector$FromUrl);
_.removeTag = false;
var Lcom_google_gwt_core_client_ScriptInjector$FromUrl_2_classLit = createForClass('com.google.gwt.core.client', 'ScriptInjector/FromUrl', 262);
function $clinit_AsyncFragmentLoader(){
  $clinit_AsyncFragmentLoader = emptyMethod;
  BROWSER_LOADER = new AsyncFragmentLoader(2, stampJavaTypeInfo(getClassLiteralForArray(I_classLit, 1), $intern_12, 30, 15, []), new ScriptTagLoadingStrategy);
}

function $clearRequestsAlreadyLoaded(this$static){
  var offset;
  while ($size_0(this$static.requestedExclusives) > 0 && this$static.isLoaded[$peek(this$static.requestedExclusives)]) {
    offset = $remove_4(this$static.requestedExclusives);
    offset < this$static.pendingDownloadErrorHandlers.length && (this$static.pendingDownloadErrorHandlers[offset] = null);
  }
}

function $initializeRemainingInitialFragments(this$static){
  var sp, sp$array, sp$index, sp$max;
  if (!this$static.remainingInitialFragments) {
    this$static.remainingInitialFragments = new AsyncFragmentLoader$BoundedIntQueue(this$static.initialLoadSequence.length + 1);
    for (sp$array = this$static.initialLoadSequence , sp$index = 0 , sp$max = sp$array.length; sp$index < sp$max; ++sp$index) {
      sp = sp$array[sp$index];
      $add_5(this$static.remainingInitialFragments, sp);
    }
    $add_5(this$static.remainingInitialFragments, this$static.numEntries);
  }
}

function $inject_0(this$static, splitPoint, loadErrorHandler){
  this$static.pendingDownloadErrorHandlers[splitPoint] = loadErrorHandler;
  $isInitial(this$static, splitPoint) || $add_5(this$static.requestedExclusives, splitPoint);
  $startLoadingNextFragment(this$static);
}

function $isEmpty_1(array){
  var i;
  for (i = 0; i < array.length; i++) {
    if (array[i]) {
      return false;
    }
  }
  return true;
}

function $isInitial(this$static, splitPoint){
  var sp, sp$array, sp$index, sp$max;
  if (splitPoint == this$static.numEntries) {
    return true;
  }
  for (sp$array = this$static.initialLoadSequence , sp$index = 0 , sp$max = sp$array.length; sp$index < sp$max; ++sp$index) {
    sp = sp$array[sp$index];
    if (sp == splitPoint) {
      return true;
    }
  }
  return false;
}

function $logEventProgress(eventGroup, fragment){
  !!$stats && stats($createStatsEvent(eventGroup, 'begin', fragment, -1));
}

function $onLoadImpl(this$static, fragment){
  var callback, callback$array, callback$index, callback$max, callbacks, t, logGroup;
  logGroup = fragment == this$static.numEntries?'leftoversDownload':'download' + fragment;
  !!$stats && stats($createStatsEvent(logGroup, 'end', fragment, -1));
  fragment < this$static.pendingDownloadErrorHandlers.length && (this$static.pendingDownloadErrorHandlers[fragment] = null);
  $isInitial(this$static, fragment) && !!this$static.remainingInitialFragments && $remove_4(this$static.remainingInitialFragments);
  this$static.fragmentLoading = -1;
  this$static.isLoaded[fragment] = true;
  $startLoadingNextFragment(this$static);
  callbacks = this$static.allCallbacks[fragment];
  if (callbacks != null) {
    !!$stats && stats($createStatsEvent('runCallbacks' + fragment, 'begin', -1, -1));
    this$static.allCallbacks[fragment] = null;
    for (callback$array = callbacks , callback$index = 0 , callback$max = callbacks.length; callback$index < callback$max; ++callback$index) {
      callback = callback$array[callback$index];
      try {
        castTo(callback, 299).onSuccess();
      }
       catch ($e0) {
        $e0 = toJava($e0);
        if (instanceOf($e0, 17)) {
          t = $e0;
          $clinit_Impl();
          reportUncaughtException(t, true);
        }
         else 
          throw toJs($e0);
      }
    }
    !!$stats && stats($createStatsEvent('runCallbacks' + fragment, 'end', -1, -1));
  }
}

function $runAsyncImpl(this$static, fragment, callback){
  var callbacks;
  if (this$static.isLoaded[fragment]) {
    $scheduleDeferred(($clinit_SchedulerImpl() , INSTANCE_29), new OnSuccessExecutor$1(callback));
    return;
  }
  callbacks = this$static.allCallbacks[fragment];
  callbacks == null && (callbacks = this$static.allCallbacks[fragment] = initUnidimensionalArray(Lcom_google_gwt_core_client_RunAsyncCallback_2_classLit, $intern_13, 299, 0, 0, 1));
  setCheck(callbacks, callbacks.length, callback);
  !!this$static.pendingDownloadErrorHandlers[fragment] || $inject_0(this$static, fragment, new AsyncFragmentLoader$1(this$static, fragment));
}

function $startLoadingFragment(this$static, fragment){
  this$static.fragmentLoading = fragment;
  $logEventProgress(fragment == this$static.numEntries?'leftoversDownload':'download' + fragment, fragment);
  $startLoadingFragment_0(this$static.loadingStrategy, fragment, new AsyncFragmentLoader$ResetAfterDownloadFailure(this$static, fragment));
}

function $startLoadingNextFragment(this$static){
  if (this$static.fragmentLoading >= 0) {
    return;
  }
  $initializeRemainingInitialFragments(this$static);
  $clearRequestsAlreadyLoaded(this$static);
  if ($isEmpty_1(this$static.pendingDownloadErrorHandlers)) {
    return;
  }
  if ($size_0(this$static.remainingInitialFragments) > 0) {
    $startLoadingFragment(this$static, $peek(this$static.remainingInitialFragments));
    return;
  }
  if ($size_0(this$static.requestedExclusives) > 0) {
    $startLoadingFragment(this$static, $remove_4(this$static.requestedExclusives));
    return;
  }
}

function AsyncFragmentLoader(numEntries, initialLoadSequence, loadingStrategy){
  var numEntriesPlusOne;
  this.numEntries = numEntries;
  this.initialLoadSequence = initialLoadSequence;
  this.loadingStrategy = loadingStrategy;
  numEntriesPlusOne = numEntries + 1;
  this.allCallbacks = initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_3, 4, numEntriesPlusOne, 3, 2);
  this.requestedExclusives = new AsyncFragmentLoader$BoundedIntQueue(numEntriesPlusOne);
  this.isLoaded = initUnidimensionalArray(Z_classLit, $intern_14, 30, numEntriesPlusOne, 16, 1);
  this.pendingDownloadErrorHandlers = initUnidimensionalArray(Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit, $intern_13, 304, numEntriesPlusOne, 0, 1);
}

function onLoad_1(fragment){
  $clinit_AsyncFragmentLoader();
  $onLoadImpl(BROWSER_LOADER, fragment);
}

function runAsync(fragment, callback){
  $clinit_AsyncFragmentLoader();
  $runAsyncImpl(BROWSER_LOADER, fragment, callback);
}

defineClass(404, 1, {}, AsyncFragmentLoader);
_.fragmentLoading = -1;
_.numEntries = 0;
_.remainingInitialFragments = null;
var BROWSER_LOADER;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader', 404);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit = createForInterface('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/LoadTerminatedHandler');
function AsyncFragmentLoader$1(this$0, val$fragment){
  this.this$01 = this$0;
  this.val$fragment2 = val$fragment;
}

defineClass(406, 1, {304:1}, AsyncFragmentLoader$1);
_.loadTerminated = function loadTerminated(reason){
  var callback, callback$index, callback$max, callbacks;
  callbacks = this.this$01.allCallbacks[this.val$fragment2];
  if (callbacks != null) {
    this.this$01.allCallbacks[this.val$fragment2] = null;
    for (callback$index = 0 , callback$max = callbacks.length; callback$index < callback$max; ++callback$index) {
      callback = callbacks[callback$index];
      castTo(callback, 299);
      showFeatureNotSupported('Cannot load application from server', false);
    }
  }
}
;
_.val$fragment2 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/1', 406);
function $add_5(this$static, x_0){
  this$static.array[this$static.write_0++] = x_0;
}

function $clear(this$static){
  this$static.read = 0;
  this$static.write_0 = 0;
}

function $peek(this$static){
  return this$static.array[this$static.read];
}

function $remove_4(this$static){
  return this$static.array[this$static.read++];
}

function $size_0(this$static){
  return this$static.write_0 - this$static.read;
}

function AsyncFragmentLoader$BoundedIntQueue(maxPuts){
  this.array = initUnidimensionalArray(I_classLit, $intern_12, 30, maxPuts, 15, 1);
}

defineClass(303, 1, {}, AsyncFragmentLoader$BoundedIntQueue);
_.read = 0;
_.write_0 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$BoundedIntQueue_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/BoundedIntQueue', 303);
function AsyncFragmentLoader$HttpDownloadFailure(url_0){
  RuntimeException_0.call(this, 'Download of ' + url_0 + ' failed with status ' + 404 + '(' + 'Script Tag Failure - no status available' + ')');
}

defineClass(408, 10, $intern_5, AsyncFragmentLoader$HttpDownloadFailure);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$HttpDownloadFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/HttpDownloadFailure', 408);
function AsyncFragmentLoader$HttpInstallFailure(url_0, text_0, rootCause){
  RuntimeException_1.call(this, 'Install of ' + url_0 + ' failed with text ' + text_0, rootCause);
}

defineClass(409, 10, $intern_5, AsyncFragmentLoader$HttpInstallFailure);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$HttpInstallFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/HttpInstallFailure', 409);
function $loadTerminated(this$static, reason){
  var e, handler, handler$array, handler$index, handler$max, handlersToRun, lastException;
  if (this$static.this$01.fragmentLoading != this$static.fragment_0) {
    return;
  }
  handlersToRun = this$static.this$01.pendingDownloadErrorHandlers;
  this$static.this$01.pendingDownloadErrorHandlers = initUnidimensionalArray(Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit, $intern_13, 304, this$static.this$01.numEntries + 1, 0, 1);
  $clear(this$static.this$01.requestedExclusives);
  this$static.this$01.fragmentLoading = -1;
  lastException = null;
  for (handler$array = handlersToRun , handler$index = 0 , handler$max = handlersToRun.length; handler$index < handler$max; ++handler$index) {
    handler = handler$array[handler$index];
    if (handler) {
      try {
        handler.loadTerminated(reason);
      }
       catch ($e0) {
        $e0 = toJava($e0);
        if (instanceOf($e0, 10)) {
          e = $e0;
          lastException = e;
        }
         else 
          throw toJs($e0);
      }
    }
  }
  if (lastException) {
    throw toJs(lastException);
  }
}

function AsyncFragmentLoader$ResetAfterDownloadFailure(this$0, myFragment){
  this.this$01 = this$0;
  this.fragment_0 = myFragment;
}

defineClass(407, 1, {304:1}, AsyncFragmentLoader$ResetAfterDownloadFailure);
_.loadTerminated = function loadTerminated_0(reason){
  $loadTerminated(this, reason);
}
;
_.fragment_0 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$ResetAfterDownloadFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/ResetAfterDownloadFailure', 407);
function $createStatsEvent(eventGroup, type_0, fragment, size_0){
  var evt = {moduleName:($clinit_Impl() , $moduleName), sessionId:$sessionId, subSystem:'runAsync', evtGroup:eventGroup, millis:(new Date).getTime(), type:type_0};
  fragment >= 0 && (evt.fragment = fragment);
  size_0 >= 0 && (evt.size = size_0);
  return evt;
}

function stats(data_0){
  return $stats(data_0);
}

function $clinit_Impl(){
  $clinit_Impl = emptyMethod;
  !!($clinit_StackTraceCreator() , collector);
}

function apply_0(jsFunction, thisObj, args){
  return jsFunction.apply(thisObj, args);
  var __0;
}

function enter(){
  var now_0;
  if (entryDepth != 0) {
    now_0 = now_1();
    if (now_0 - watchdogEntryDepthLastScheduled > 2000) {
      watchdogEntryDepthLastScheduled = now_0;
      watchdogEntryDepthTimerId = $wnd.setTimeout(watchdogEntryDepthRun, 10);
    }
  }
  if (entryDepth++ == 0) {
    $flushEntryCommands(($clinit_SchedulerImpl() , INSTANCE_29));
    return true;
  }
  return false;
}

function entry_0(jsFunction){
  $clinit_Impl();
  return function(){
    return entry0(jsFunction, this, arguments);
    var __0;
  }
  ;
}

function entry0(jsFunction, thisObj, args){
  var initialEntry, t;
  initialEntry = enter();
  try {
    if (uncaughtExceptionHandler) {
      try {
        return apply_0(jsFunction, thisObj, args);
      }
       catch ($e0) {
        $e0 = toJava($e0);
        if (instanceOf($e0, 17)) {
          t = $e0;
          reportUncaughtException(t, true);
          return undefined;
        }
         else 
          throw toJs($e0);
      }
    }
     else {
      return apply_0(jsFunction, thisObj, args);
    }
  }
   finally {
    exit(initialEntry);
  }
}

function exit(initialEntry){
  initialEntry && $flushFinallyCommands(($clinit_SchedulerImpl() , INSTANCE_29));
  --entryDepth;
  if (initialEntry) {
    if (watchdogEntryDepthTimerId != -1) {
      watchdogEntryDepthCancel(watchdogEntryDepthTimerId);
      watchdogEntryDepthTimerId = -1;
    }
  }
}

function maybeInitializeWindowOnError(){
  $clinit_Impl();
  if (onErrorInitialized) {
    return;
  }
  onErrorInitialized = true;
  registerWindowOnError(false);
}

function registerWindowOnError(reportAlways){
  $clinit_Impl();
  function errorHandler(msg, url_0, line, column, error){
    if (!error) {
      error = msg + ' (' + url_0 + ':' + line;
      column && (error += ':' + column);
      error += ')';
    }
    var throwable = of(error);
    reportUncaughtException(throwable, false);
  }

  ;
  function addOnErrorHandler(windowRef){
    var origHandler = windowRef.onerror;
    if (origHandler && !reportAlways) {
      return;
    }
    windowRef.onerror = function(){
      errorHandler.apply(this, arguments);
      origHandler && origHandler.apply(this, arguments);
      return false;
    }
    ;
  }

  addOnErrorHandler($wnd);
  addOnErrorHandler(window);
}

function reportToBrowser(e){
  $wnd.setTimeout(function(){
    throw e;
  }
  , 0);
}

function reportUncaughtException(e, reportSwallowedExceptionToBrowser){
  $clinit_Impl();
  var handler;
  handler = uncaughtExceptionHandler;
  if (handler) {
    if (handler == uncaughtExceptionHandlerForTest) {
      return;
    }
    $log(handler.val$log2, ($clinit_Level() , SEVERE), e.getMessage(), e);
    return;
  }
  if (reportSwallowedExceptionToBrowser) {
    reportToBrowser(instanceOf(e, 99)?castTo(e, 99).getThrown():e);
  }
   else {
    $clinit_System();
    $printStackTraceImpl(e, err, '', '');
  }
}

function watchdogEntryDepthCancel(timerId){
  $wnd.clearTimeout(timerId);
}

function watchdogEntryDepthRun(){
  entryDepth != 0 && (entryDepth = 0);
  watchdogEntryDepthTimerId = -1;
}

var entryDepth = 0, onErrorInitialized = false, uncaughtExceptionHandlerForTest, watchdogEntryDepthLastScheduled = 0, watchdogEntryDepthTimerId = -1;
function $startLoadingFragment_0(this$static, fragment, loadErrorHandler){
  var connector, manualRetry, request, url_0, ser;
  url_0 = gwtStartLoadingFragment(fragment, loadErrorHandler);
  if (url_0 == null) {
    return;
  }
  manualRetry = (ser = $get_2(this$static.manualRetryNumbers, fragment) , this$static.manualRetryNumbers[fragment] = ser + 1 , ser);
  if (manualRetry > 0) {
    connector = url_0.indexOf('?') != -1?38:63;
    url_0 += String.fromCharCode(connector) + 'manualRetry=' + manualRetry;
  }
  request = new LoadingStrategyBase$RequestData(url_0, loadErrorHandler, fragment, MAX_AUTO_RETRY_COUNT);
  setAsyncCallback(request.fragment_0, request);
  $inject($setCallback($setRemoveTag(($clinit_ScriptInjector() , new ScriptInjector$FromUrl(request.url_0))), new ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1(request)));
}

function gwtStartLoadingFragment(fragment, loadErrorHandler){
  function loadFailed(e){
    loadErrorHandler.loadTerminated(e);
  }

  return __gwtStartLoadingFragment(fragment, $entry(loadFailed));
}

defineClass(418, 1, {});
var MAX_AUTO_RETRY_COUNT = 3;
var Lcom_google_gwt_core_client_impl_LoadingStrategyBase_2_classLit = createForClass('com.google.gwt.core.client.impl', 'LoadingStrategyBase', 418);
function $get_2(this$static, x_0){
  return this$static[x_0]?this$static[x_0]:0;
}

function $onLoadError(this$static, e, mayRetry){
  var connector;
  if (mayRetry) {
    ++this$static.retryCount;
    if (this$static.retryCount <= this$static.maxRetryCount) {
      connector = this$static.originalUrl.indexOf('?') != -1?38:63;
      this$static.url_0 = this$static.originalUrl + String.fromCharCode(connector) + 'autoRetry=' + this$static.retryCount;
      setAsyncCallback(this$static.fragment_0, this$static);
      $inject($setCallback($setRemoveTag(($clinit_ScriptInjector() , new ScriptInjector$FromUrl(this$static.url_0))), new ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1(this$static)));
      return;
    }
  }
  $loadTerminated(this$static.errorHandler, e);
}

function $tryInstall(this$static, code_0){
  var e, textIntro;
  try {
    __gwtInstallCode(code_0);
  }
   catch ($e0) {
    $e0 = toJava($e0);
    if (instanceOf($e0, 10)) {
      e = $e0;
      textIntro = code_0;
      code_0 != null && code_0.length > 200 && (textIntro = code_0.substr(0, 200) + '...');
      $onLoadError(this$static, new AsyncFragmentLoader$HttpInstallFailure(this$static.url_0, textIntro, e), false);
    }
     else 
      throw toJs($e0);
  }
}

function LoadingStrategyBase$RequestData(url_0, errorHandler, fragment, maxRetryCount){
  this.url_0 = url_0;
  this.originalUrl = url_0;
  this.errorHandler = errorHandler;
  this.maxRetryCount = maxRetryCount;
  this.retryCount = 0;
  this.fragment_0 = fragment;
}

defineClass(419, 1, {}, LoadingStrategyBase$RequestData);
_.errorHandler = null;
_.fragment_0 = 0;
_.maxRetryCount = 0;
_.retryCount = 0;
var Lcom_google_gwt_core_client_impl_LoadingStrategyBase$RequestData_2_classLit = createForClass('com.google.gwt.core.client.impl', 'LoadingStrategyBase/RequestData', 419);
function OnSuccessExecutor$1(val$callback){
  this.val$callback3 = val$callback;
}

defineClass(405, 1, {}, OnSuccessExecutor$1);
_.execute = function execute_19(){
  this.val$callback3.onSuccess();
}
;
var Lcom_google_gwt_core_client_impl_OnSuccessExecutor$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'OnSuccessExecutor/1', 405);
function $clinit_SchedulerImpl(){
  $clinit_SchedulerImpl = emptyMethod;
  INSTANCE_29 = new SchedulerImpl;
}

function $flushEntryCommands(this$static){
  var oldQueue, rescheduled;
  if (this$static.entryCommands) {
    rescheduled = null;
    do {
      oldQueue = this$static.entryCommands;
      this$static.entryCommands = null;
      rescheduled = runScheduledTasks(oldQueue, rescheduled);
    }
     while (this$static.entryCommands);
    this$static.entryCommands = rescheduled;
  }
}

function $flushFinallyCommands(this$static){
  var oldQueue, rescheduled;
  if (this$static.finallyCommands) {
    rescheduled = null;
    do {
      oldQueue = this$static.finallyCommands;
      this$static.finallyCommands = null;
      rescheduled = runScheduledTasks(oldQueue, rescheduled);
    }
     while (this$static.finallyCommands);
    this$static.finallyCommands = rescheduled;
  }
}

function $flushPostEventPumpCommands(this$static){
  var oldDeferred;
  if (this$static.deferredCommands) {
    oldDeferred = this$static.deferredCommands;
    this$static.deferredCommands = null;
    !this$static.incrementalCommands && (this$static.incrementalCommands = []);
    runScheduledTasks(oldDeferred, this$static.incrementalCommands);
  }
  !!this$static.incrementalCommands && (this$static.incrementalCommands = $runRepeatingTasks(this$static.incrementalCommands));
}

function $isWorkQueued(this$static){
  return !!this$static.deferredCommands || !!this$static.incrementalCommands;
}

function $maybeSchedulePostEventPumpCommands(this$static){
  if (!this$static.shouldBeRunning) {
    this$static.shouldBeRunning = true;
    !this$static.flusher && (this$static.flusher = new SchedulerImpl$Flusher(this$static));
    scheduleFixedDelayImpl(this$static.flusher, 1);
    !this$static.rescue && (this$static.rescue = new SchedulerImpl$Rescuer(this$static));
    scheduleFixedDelayImpl(this$static.rescue, 50);
  }
}

function $runRepeatingTasks(tasks){
  var canceledSomeTasks, duration, executedSomeTask, i, length_0, newTasks, t;
  length_0 = tasks.length;
  if (length_0 == 0) {
    return null;
  }
  canceledSomeTasks = false;
  duration = new Duration;
  while (now_1() - duration.start_0 < 16) {
    executedSomeTask = false;
    for (i = 0; i < length_0; i++) {
      t = tasks[i];
      if (!t) {
        continue;
      }
      executedSomeTask = true;
      if (!t[0].execute_1()) {
        tasks[i] = null;
        canceledSomeTasks = true;
      }
    }
    if (!executedSomeTask) {
      break;
    }
  }
  if (canceledSomeTasks) {
    newTasks = [];
    for (i = 0; i < length_0; i++) {
      !!tasks[i] && (newTasks[newTasks.length] = tasks[i] , undefined);
    }
    return newTasks.length == 0?null:newTasks;
  }
   else {
    return tasks;
  }
}

function $scheduleDeferred(this$static, cmd){
  this$static.deferredCommands = push_0(this$static.deferredCommands, [cmd, false]);
  $maybeSchedulePostEventPumpCommands(this$static);
}

function SchedulerImpl(){
}

function execute_20(cmd){
  return cmd.execute_1();
}

function push_0(queue, task){
  !queue && (queue = []);
  queue[queue.length] = task;
  return queue;
}

function runScheduledTasks(tasks, rescheduled){
  var e, i, j, t;
  for (i = 0 , j = tasks.length; i < j; i++) {
    t = tasks[i];
    try {
      t[1]?t[0].execute_1() && (rescheduled = push_0(rescheduled, t)):t[0].execute();
    }
     catch ($e0) {
      $e0 = toJava($e0);
      if (instanceOf($e0, 17)) {
        e = $e0;
        $clinit_Impl();
        reportUncaughtException(e, true);
      }
       else 
        throw toJs($e0);
    }
  }
  return rescheduled;
}

function scheduleFixedDelayImpl(cmd, delayMs){
  $clinit_SchedulerImpl();
  function callback(){
    var ret = $entry(execute_20)(cmd);
    ret && $wnd.setTimeout(callback, delayMs);
  }

  $wnd.setTimeout(callback, delayMs);
}

defineClass(493, 996, {}, SchedulerImpl);
_.flushRunning = false;
_.shouldBeRunning = false;
var INSTANCE_29;
var Lcom_google_gwt_core_client_impl_SchedulerImpl_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl', 493);
function SchedulerImpl$Flusher(this$0){
  this.this$01 = this$0;
}

defineClass(494, 1, {}, SchedulerImpl$Flusher);
_.execute_1 = function execute_21(){
  this.this$01.flushRunning = true;
  $flushPostEventPumpCommands(this.this$01);
  this.this$01.flushRunning = false;
  return this.this$01.shouldBeRunning = $isWorkQueued(this.this$01);
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Flusher_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Flusher', 494);
function SchedulerImpl$Rescuer(this$0){
  this.this$01 = this$0;
}

defineClass(495, 1, {}, SchedulerImpl$Rescuer);
_.execute_1 = function execute_22(){
  this.this$01.flushRunning && scheduleFixedDelayImpl(this.this$01.flusher, 1);
  return this.this$01.shouldBeRunning;
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Rescuer_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Rescuer', 495);
function ScriptTagLoadingStrategy(){
  this.manualRetryNumbers = [];
}

function asyncCallback(request, code_0){
  var firstTimeCalled;
  firstTimeCalled = clearAsyncCallback(request.fragment_0);
  firstTimeCalled && $tryInstall(request, code_0);
}

function cleanup(request){
  var neverCalled;
  neverCalled = clearAsyncCallback(request.fragment_0);
  neverCalled && $onLoadError(request, new AsyncFragmentLoader$HttpDownloadFailure(request.url_0), true);
}

function clearAsyncCallback(fragment){
  if (!__gwtModuleFunction['runAsyncCallback' + fragment]) {
    return false;
  }
  delete __gwtModuleFunction['runAsyncCallback' + fragment];
  return true;
}

function setAsyncCallback(fragment, request){
  __gwtModuleFunction['runAsyncCallback' + fragment] = $entry(function(code_0, instance){
    asyncCallback(request, code_0);
  }
  );
}

defineClass(403, 418, {}, ScriptTagLoadingStrategy);
var Lcom_google_gwt_core_client_impl_ScriptTagLoadingStrategy_2_classLit = createForClass('com.google.gwt.core.client.impl', 'ScriptTagLoadingStrategy', 403);
function ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1(val$request){
  this.val$request2 = val$request;
}

defineClass(309, 1, {}, ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1);
_.onFailure = function onFailure(reason){
  var lastArg;
  cleanup((lastArg = this , castTo(reason, 14) , lastArg).val$request2);
}
;
_.onSuccess_0 = function onSuccess_0(result){
  var lastArg;
  cleanup((lastArg = this , throwClassCastExceptionUnlessNull(result) , lastArg).val$request2);
}
;
var Lcom_google_gwt_core_client_impl_ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'ScriptTagLoadingStrategy/ScriptTagDownloadStrategy/1', 309);
function $clinit_StackTraceCreator(){
  $clinit_StackTraceCreator = emptyMethod;
  var c, enforceLegacy;
  enforceLegacy = !supportsErrorStack();
  c = new StackTraceCreator$CollectorModernNoSourceMap;
  collector = enforceLegacy?new StackTraceCreator$CollectorLegacy:c;
}

function captureStackTrace(error){
  $clinit_StackTraceCreator();
  collector.collect(error);
}

function dropInternalFrames(stackTrace){
  var dropFrameUntilFnName, dropFrameUntilFnName2, i, numberOfFramesToSearch;
  dropFrameUntilFnName = 'captureStackTrace';
  dropFrameUntilFnName2 = 'initializeBackingError';
  numberOfFramesToSearch = $wnd.Math.min(stackTrace.length, 5);
  for (i = numberOfFramesToSearch - 1; i >= 0; i--) {
    if ($equals_6(stackTrace[i].methodName, dropFrameUntilFnName) || $equals_6(stackTrace[i].methodName, dropFrameUntilFnName2)) {
      stackTrace.length >= i + 1 && stackTrace.splice(0, i + 1);
      break;
    }
  }
  return stackTrace;
}

function extractFunctionName(fnName){
  var fnRE = /function(?:\s+([\w$]+))?\s*\(/;
  var match_0 = fnRE.exec(fnName);
  return match_0 && match_0[1] || 'anonymous';
}

function parseInt_0(number){
  $clinit_StackTraceCreator();
  return parseInt(number) || -1;
}

function supportsErrorStack(){
  if (Error.stackTraceLimit > 0) {
    $wnd.Error.stackTraceLimit = Error.stackTraceLimit = 64;
    return true;
  }
  return 'stack' in new Error;
}

var collector;
defineClass(1011, 1, {});
var Lcom_google_gwt_core_client_impl_StackTraceCreator$Collector_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/Collector', 1011);
function StackTraceCreator$CollectorLegacy(){
}

defineClass(416, 1011, {}, StackTraceCreator$CollectorLegacy);
_.collect = function collect(error){
  var seen = {}, name_1;
  var fnStack = [];
  error['fnStack'] = fnStack;
  var callee = arguments.callee.caller;
  while (callee) {
    var name_0 = ($clinit_StackTraceCreator() , callee.name || (callee.name = extractFunctionName(callee.toString())));
    fnStack.push(name_0);
    var keyName = ':' + name_0;
    var withThisName = seen[keyName];
    if (withThisName) {
      var i, j;
      for (i = 0 , j = withThisName.length; i < j; i++) {
        if (withThisName[i] === callee) {
          return;
        }
      }
    }
    (withThisName || (seen[keyName] = [])).push(callee);
    callee = callee.caller;
  }
}
;
_.getStackTrace = function getStackTrace(t){
  var i, length_0, stack_0, stackTrace;
  stack_0 = ($clinit_StackTraceCreator() , t && t['fnStack']?t['fnStack']:[]);
  length_0 = stack_0.length;
  stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_3, 121, length_0, 0, 1);
  for (i = 0; i < length_0; i++) {
    stackTrace[i] = new StackTraceElement(stack_0[i], null, -1);
  }
  return stackTrace;
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorLegacy_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorLegacy', 416);
function $parse_1(this$static, stString){
  var closeParen, col, endFileUrlIndex, fileName, index_0, lastColonIndex, line, location_0, toReturn;
  if (stString.length == 0) {
    return this$static.createSte('Unknown', 'anonymous', -1, -1);
  }
  toReturn = $trim(stString);
  $equals_6(toReturn.substr(0, 3), 'at ') && (toReturn = toReturn.substr(3));
  toReturn = toReturn.replace(/\[.*?\]/g, '');
  index_0 = toReturn.indexOf('(');
  if (index_0 == -1) {
    index_0 = toReturn.indexOf('@');
    if (index_0 == -1) {
      location_0 = toReturn;
      toReturn = '';
    }
     else {
      location_0 = $trim(toReturn.substr(index_0 + 1));
      toReturn = $trim(toReturn.substr(0, index_0));
    }
  }
   else {
    closeParen = toReturn.indexOf(')', index_0);
    location_0 = toReturn.substr(index_0 + 1, closeParen - (index_0 + 1));
    toReturn = $trim(toReturn.substr(0, index_0));
  }
  index_0 = $indexOf_0(toReturn, fromCodePoint(46));
  index_0 != -1 && (toReturn = toReturn.substr(index_0 + 1));
  (toReturn.length == 0 || $equals_6(toReturn, 'Anonymous function')) && (toReturn = 'anonymous');
  lastColonIndex = $lastIndexOf(location_0, fromCodePoint(58));
  endFileUrlIndex = $lastIndexOf_0(location_0, fromCodePoint(58), lastColonIndex - 1);
  line = -1;
  col = -1;
  fileName = 'Unknown';
  if (lastColonIndex != -1 && endFileUrlIndex != -1) {
    fileName = location_0.substr(0, endFileUrlIndex);
    line = parseInt_0(location_0.substr(endFileUrlIndex + 1, lastColonIndex - (endFileUrlIndex + 1)));
    col = parseInt_0(location_0.substr(lastColonIndex + 1));
  }
  return this$static.createSte(fileName, toReturn, line, col);
}

defineClass(1012, 1011, {});
_.collect = function collect_0(error){
}
;
_.createSte = function createSte(fileName, method, line, col){
  return new StackTraceElement(method, fileName + '@' + col, line < 0?-1:line);
}
;
_.getStackTrace = function getStackTrace_0(t){
  var addIndex, i, length_0, stack_0, stackTrace, ste, e;
  stack_0 = ($clinit_StackTraceCreator() , e = t.backingJsObject , e && e.stack?e.stack.split('\n'):[]);
  stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_3, 121, 0, 0, 1);
  addIndex = 0;
  length_0 = stack_0.length;
  if (length_0 == 0) {
    return stackTrace;
  }
  ste = $parse_1(this, stack_0[0]);
  $equals_6(ste.methodName, 'anonymous') || (stackTrace[addIndex++] = ste);
  for (i = 1; i < length_0; i++) {
    stackTrace[addIndex++] = $parse_1(this, stack_0[i]);
  }
  return stackTrace;
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModern_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModern', 1012);
function StackTraceCreator$CollectorModernNoSourceMap(){
}

defineClass(417, 1012, {}, StackTraceCreator$CollectorModernNoSourceMap);
_.createSte = function createSte_0(fileName, method, line, col){
  return new StackTraceElement(method, fileName, -1);
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModernNoSourceMap_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModernNoSourceMap', 417);
function $appendChild(this$static, newChild){
  return this$static.appendChild(newChild);
}

function $removeChild(this$static, oldChild){
  return this$static.removeChild(oldChild);
}

function $setInnerHTML(this$static, html){
  this$static.innerHTML = html || '';
}

function $getParentElement(node){
  var parent_0 = node.parentNode;
  (!parent_0 || parent_0.nodeType != 1) && (parent_0 = null);
  return parent_0;
}

function $setInnerText(elem, text_0){
  elem.textContent = text_0 || '';
}

function $createScriptElement(doc, source){
  var elem;
  elem = doc.createElement('script');
  elem.textContent = source || '';
  return elem;
}

function isOrHasChildImpl(parent_0, child){
  if (parent_0.nodeType != 1 && parent_0.nodeType != 9) {
    return parent_0 == child;
  }
  if (child.nodeType != 1) {
    child = child.parentNode;
    if (!child) {
      return false;
    }
  }
  if (parent_0.nodeType == 9) {
    return parent_0 === child || parent_0.body && parent_0.body.contains(child);
  }
   else {
    return parent_0 === child || parent_0.contains(child);
  }
}

function $getElementById(this$static, elementId){
  return this$static.getElementById(elementId);
}

defineClass(398, 1, {});
_.hashCode_0 = function hashCode_15(){
  return this.index_0;
}
;
_.toString_0 = function toString_34(){
  return 'Event type';
}
;
_.index_0 = 0;
var nextHashCode = 0;
var Lcom_google_web_bindery_event_shared_Event$Type_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event/Type', 398);
function GwtEvent$Type(){
  this.index_0 = ++nextHashCode;
}

defineClass(122, 398, {}, GwtEvent$Type);
var Lcom_google_gwt_event_shared_GwtEvent$Type_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent/Type', 122);
function AttachEvent(attached){
  this.attached = attached;
}

function fire_0(source, attached){
  var event_0;
  if (TYPE_23) {
    event_0 = new AttachEvent(attached);
    source.fireEvent(event_0);
  }
}

defineClass(611, 998, {}, AttachEvent);
_.dispatch_0 = function dispatch_24(handler){
  castTo(handler, 1059).onAttachOrDetach(this);
}
;
_.getAssociatedType = function getAssociatedType_24(){
  return TYPE_23;
}
;
_.attached = false;
var TYPE_23;
var Lcom_google_gwt_event_logical_shared_AttachEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'AttachEvent', 611);
function CloseEvent_0(){
}

function fire_1(source){
  var event_0;
  if (TYPE_24) {
    event_0 = new CloseEvent_0;
    source.fireEvent(event_0);
  }
}

defineClass(609, 998, {}, CloseEvent_0);
_.dispatch_0 = function dispatch_25(handler){
  castTo(handler, 982).onClose(this);
}
;
_.getAssociatedType = function getAssociatedType_25(){
  return TYPE_24;
}
;
var TYPE_24;
var Lcom_google_gwt_event_logical_shared_CloseEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'CloseEvent', 609);
defineClass($intern_15, 1, {});
var Lcom_google_web_bindery_event_shared_EventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'EventBus', $intern_15);
function $addHandler_0(this$static, type_0, handler){
  return new LegacyHandlerWrapper($doAdd(this$static.eventBus, type_0, null, handler));
}

function $fireEvent(this$static, event_0){
  var e, oldSource;
  !event_0.dead || event_0.revive();
  oldSource = event_0.source;
  $overrideSource(event_0, this$static.source);
  try {
    $doFire(this$static.eventBus, event_0, null);
  }
   catch ($e0) {
    $e0 = toJava($e0);
    if (instanceOf($e0, 131)) {
      e = $e0;
      throw toJs(new UmbrellaException_0(e.causes));
    }
     else 
      throw toJs($e0);
  }
   finally {
    oldSource == null?(event_0.dead = true , event_0.source = null):(event_0.source = oldSource);
  }
}

function $isEventHandled(this$static, e){
  return $isEventHandled_0(this$static.eventBus, e);
}

function HandlerManager(source){
  HandlerManager_0.call(this, source, false);
}

function HandlerManager_0(source, fireInReverseOrder){
  this.eventBus = new HandlerManager$Bus(fireInReverseOrder);
  this.source = source;
}

defineClass(165, 1, $intern_16, HandlerManager, HandlerManager_0);
_.fireEvent = function fireEvent_1(event_0){
  $fireEvent(this, event_0);
}
;
var Lcom_google_gwt_event_shared_HandlerManager_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager', 165);
function $defer(this$static, command){
  !this$static.deferredDeltas && (this$static.deferredDeltas = new ArrayList);
  $add_13(this$static.deferredDeltas, command);
}

function $doAdd(this$static, type_0, source, handler){
  var l;
  if (!type_0) {
    throw toJs(new NullPointerException_1('Cannot add a handler with a null type'));
  }
  if (!handler) {
    throw toJs(new NullPointerException_1('Cannot add a null handler'));
  }
  this$static.firingDepth > 0?$defer(this$static, new SimpleEventBus$2(this$static, type_0, source, handler)):(l = $ensureHandlerList(this$static, type_0, source) , l.add_0(handler));
  return new SimpleEventBus$1(this$static, type_0, source, handler);
}

function $doAddNow(this$static, type_0, source, handler){
  var l;
  l = $ensureHandlerList(this$static, type_0, source);
  l.add_0(handler);
}

function $doFire(this$static, event_0, source){
  var causes, e, handler, handlers, it;
  if (!event_0) {
    throw toJs(new NullPointerException_1('Cannot fire null event'));
  }
  try {
    ++this$static.firingDepth;
    source != null && (event_0.source = source);
    handlers = $getDispatchList(this$static, event_0.getAssociatedType(), source);
    causes = null;
    it = this$static.isReverseOrder?handlers.listIterator_0(handlers.size_1()):handlers.listIterator();
    while (this$static.isReverseOrder?it.hasPrevious():it.hasNext_0()) {
      handler = this$static.isReverseOrder?it.previous():it.next_1();
      try {
        event_0.dispatch_0(castTo(handler, 27));
      }
       catch ($e0) {
        $e0 = toJava($e0);
        if (instanceOf($e0, 17)) {
          e = $e0;
          !causes && (causes = new HashSet);
          $put(causes.map_0, e, causes);
        }
         else 
          throw toJs($e0);
      }
    }
    if (causes) {
      throw toJs(new UmbrellaException(causes));
    }
  }
   finally {
    --this$static.firingDepth;
    this$static.firingDepth == 0 && $handleQueuedAddsAndRemoves(this$static);
  }
}

function $ensureHandlerList(this$static, type_0, source){
  var handlers, sourceMap;
  sourceMap = castTo($get_0(this$static.map_0, type_0), 81);
  if (!sourceMap) {
    sourceMap = new HashMap;
    $put(this$static.map_0, type_0, sourceMap);
  }
  handlers = castTo(sourceMap.get_1(source), 40);
  if (!handlers) {
    handlers = new ArrayList;
    sourceMap.put(source, handlers);
  }
  return handlers;
}

function $getDispatchList(this$static, type_0, source){
  var directHandlers, globalHandlers, rtn;
  directHandlers = $getHandlerList(this$static, type_0, source);
  if (source == null) {
    return directHandlers;
  }
  globalHandlers = $getHandlerList(this$static, type_0, null);
  rtn = new ArrayList_1(directHandlers);
  $addAll_1(rtn, globalHandlers);
  return rtn;
}

function $getHandlerList(this$static, type_0, source){
  var handlers, sourceMap;
  sourceMap = castTo($get_0(this$static.map_0, type_0), 81);
  if (!sourceMap) {
    return $clinit_Collections() , $clinit_Collections() , EMPTY_LIST;
  }
  handlers = castTo(sourceMap.get_1(source), 40);
  if (!handlers) {
    return $clinit_Collections() , $clinit_Collections() , EMPTY_LIST;
  }
  return handlers;
}

function $handleQueuedAddsAndRemoves(this$static){
  var c, c$iterator;
  if (this$static.deferredDeltas) {
    try {
      for (c$iterator = new ArrayList$1(this$static.deferredDeltas); c$iterator.i < c$iterator.this$01.array.length;) {
        c = castTo($next_3(c$iterator), 402);
        c.execute();
      }
    }
     finally {
      this$static.deferredDeltas = null;
    }
  }
}

function $isEventHandled_0(this$static, eventKey){
  return $containsKey_0(this$static.map_0, eventKey);
}

function SimpleEventBus_0(fireInReverseOrder){
  this.map_0 = new HashMap;
  this.isReverseOrder = fireInReverseOrder;
}

defineClass(247, $intern_15, {});
_.firingDepth = 0;
_.isReverseOrder = false;
var Lcom_google_web_bindery_event_shared_SimpleEventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus', 247);
function HandlerManager$Bus(fireInReverseOrder){
  SimpleEventBus_0.call(this, fireInReverseOrder);
}

defineClass(399, 247, {}, HandlerManager$Bus);
var Lcom_google_gwt_event_shared_HandlerManager$Bus_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager/Bus', 399);
function LegacyHandlerWrapper(real){
  this.real = real;
}

defineClass(301, 1, {981:1}, LegacyHandlerWrapper);
var Lcom_google_gwt_event_shared_LegacyHandlerWrapper_2_classLit = createForClass('com.google.gwt.event.shared', 'LegacyHandlerWrapper', 301);
function UmbrellaException(causes){
  var cause, cause$iterator, i;
  RuntimeException_1.call(this, makeMessage(causes), causes.isEmpty()?null:castTo(causes.iterator().next_1(), 17));
  this.causes = causes;
  i = 0;
  for (cause$iterator = causes.iterator(); cause$iterator.hasNext_0();) {
    cause = castTo(cause$iterator.next_1(), 17);
    if (i++ == 0) {
      continue;
    }
    $addSuppressed(this, cause);
  }
}

function makeMessage(causes){
  var b, count, first, t, t$iterator;
  count = causes.size_1();
  if (count == 0) {
    return null;
  }
  b = new StringBuilder_1(count == 1?'Exception caught: ':count + ' exceptions caught: ');
  first = true;
  for (t$iterator = causes.iterator(); t$iterator.hasNext_0();) {
    t = castTo(t$iterator.next_1(), 17);
    first?(first = false):(b.string += '; ' , b);
    $append_5(b, t.getMessage());
  }
  return b.string;
}

defineClass(131, 10, $intern_17, UmbrellaException);
var Lcom_google_web_bindery_event_shared_UmbrellaException_2_classLit = createForClass('com.google.web.bindery.event.shared', 'UmbrellaException', 131);
function UmbrellaException_0(causes){
  UmbrellaException.call(this, causes);
}

defineClass(217, 131, $intern_17, UmbrellaException_0);
var Lcom_google_gwt_event_shared_UmbrellaException_2_classLit = createForClass('com.google.gwt.event.shared', 'UmbrellaException', 217);
function throwIfNull(name_0, value_0){
  if (null == value_0) {
    throw toJs(new NullPointerException_1(name_0 + ' cannot be null'));
  }
}

function $clinit_LocaleInfo(){
  $clinit_LocaleInfo = emptyMethod;
  instance_5 = new LocaleInfo;
}

function LocaleInfo(){
}

defineClass(506, 1, {}, LocaleInfo);
var instance_5;
var Lcom_google_gwt_i18n_client_LocaleInfo_2_classLit = createForClass('com.google.gwt.i18n.client', 'LocaleInfo', 506);
function canSet(array, value_0){
  var elementTypeCategory;
  switch (getElementTypeCategory(array)) {
    case 6:
      return instanceOfString(value_0);
    case 7:
      return instanceOfDouble(value_0);
    case 8:
      return instanceOfBoolean(value_0);
    case 3:
      return Array.isArray(value_0) && (elementTypeCategory = getElementTypeCategory(value_0) , !(elementTypeCategory >= 14 && elementTypeCategory <= 16));
    case 11:
      return value_0 != null && typeof value_0 === 'function';
    case 12:
      return value_0 != null && (typeof value_0 === 'object' || typeof value_0 == 'function');
    case 0:
      return canCast(value_0, array.__elementTypeId$);
    case 2:
      return isJsObjectOrFunction(value_0) && !(value_0.typeMarker === typeMarkerFn);
    case 1:
      return isJsObjectOrFunction(value_0) && !(value_0.typeMarker === typeMarkerFn) || canCast(value_0, array.__elementTypeId$);
    default:return true;
  }
}

function getClassLiteralForArray(clazz, dimensions){
  return getClassLiteralForArray_0(clazz, dimensions);
}

function getElementTypeCategory(array){
  return array.__elementTypeCategory$ == null?10:array.__elementTypeCategory$;
}

function initUnidimensionalArray(leafClassLiteral, castableTypeMap, elementTypeId, length_0, elementTypeCategory, dimensions){
  var result;
  result = initializeArrayElementsWithDefaults(elementTypeCategory, length_0);
  elementTypeCategory != 10 && stampJavaTypeInfo(getClassLiteralForArray(leafClassLiteral, dimensions), castableTypeMap, elementTypeId, elementTypeCategory, result);
  return result;
}

function initializeArrayElementsWithDefaults(elementTypeCategory, length_0){
  var array = new Array(length_0);
  var initValue;
  switch (elementTypeCategory) {
    case 14:
    case 15:
      initValue = 0;
      break;
    case 16:
      initValue = false;
      break;
    default:return array;
  }
  for (var i = 0; i < length_0; ++i) {
    array[i] = initValue;
  }
  return array;
}

function isJavaArray(src_0){
  return Array.isArray(src_0) && src_0.typeMarker === typeMarkerFn;
}

function setCheck(array, index_0, value_0){
  checkCriticalArrayType(value_0 == null || canSet(array, value_0));
  return array[index_0] = value_0;
}

function stampJavaTypeInfo(arrayClass, castableTypeMap, elementTypeId, elementTypeCategory, array){
  array.___clazz = arrayClass;
  array.castableTypeMap = castableTypeMap;
  array.typeMarker = typeMarkerFn;
  array.__elementTypeId$ = elementTypeId;
  array.__elementTypeCategory$ = elementTypeCategory;
  return array;
}

function stampJavaTypeInfo_0(array, referenceType){
  getElementTypeCategory(referenceType) != 10 && stampJavaTypeInfo(getClass__Ljava_lang_Class___devirtual$(referenceType), referenceType.castableTypeMap, referenceType.__elementTypeId$, getElementTypeCategory(referenceType), array);
  return array;
}

function create_2(value_0){
  var a0, a1, a2;
  a0 = value_0 & $intern_18;
  a1 = value_0 >> 22 & $intern_18;
  a2 = value_0 < 0?$intern_19:0;
  return create0(a0, a1, a2);
}

function create0(l, m, h){
  return {l:l, m:m, h:h};
}

function negate(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_18;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_18;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_19;
  a.l = neg0;
  a.m = neg1;
  a.h = neg2;
}

function toDoubleHelper(a){
  return a.l + a.m * $intern_20 + a.h * $intern_21;
}

function compare_4(a, b){
  var a0, a1, a2, b0, b1, b2, signA, signB;
  signA = a.h >> 19;
  signB = b.h >> 19;
  if (signA != signB) {
    return signB - signA;
  }
  a2 = a.h;
  b2 = b.h;
  if (a2 != b2) {
    return a2 - b2;
  }
  a1 = a.m;
  b1 = b.m;
  if (a1 != b1) {
    return a1 - b1;
  }
  a0 = a.l;
  b0 = b.l;
  return a0 - b0;
}

function fromDouble(value_0){
  var a0, a1, a2, negative, result;
  if (isNaN(value_0)) {
    return $clinit_BigLongLib$Const() , ZERO;
  }
  if (value_0 < -9223372036854775808) {
    return $clinit_BigLongLib$Const() , MIN_VALUE;
  }
  if (value_0 >= 9223372036854775807) {
    return $clinit_BigLongLib$Const() , MAX_VALUE;
  }
  negative = false;
  if (value_0 < 0) {
    negative = true;
    value_0 = -value_0;
  }
  a2 = 0;
  if (value_0 >= $intern_21) {
    a2 = round_int(value_0 / $intern_21);
    value_0 -= a2 * $intern_21;
  }
  a1 = 0;
  if (value_0 >= $intern_20) {
    a1 = round_int(value_0 / $intern_20);
    value_0 -= a1 * $intern_20;
  }
  a0 = round_int(value_0);
  result = create0(a0, a1, a2);
  negative && negate(result);
  return result;
}

function neg(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_18;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_18;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_19;
  return create0(neg0, neg1, neg2);
}

function shru(a, n){
  var a2, res0, res1, res2;
  n &= 63;
  a2 = a.h & $intern_19;
  if (n < 22) {
    res2 = a2 >>> n;
    res1 = a.m >> n | a2 << 22 - n;
    res0 = a.l >> n | a.m << 22 - n;
  }
   else if (n < 44) {
    res2 = 0;
    res1 = a2 >>> n - 22;
    res0 = a.m >> n - 22 | a.h << 44 - n;
  }
   else {
    res2 = 0;
    res1 = 0;
    res0 = a2 >>> n - 44;
  }
  return create0(res0 & $intern_18, res1 & $intern_18, res2 & $intern_19);
}

function toDouble(a){
  if (compare_4(a, ($clinit_BigLongLib$Const() , ZERO)) < 0) {
    return -toDoubleHelper(neg(a));
  }
  return a.l + a.m * $intern_20 + a.h * $intern_21;
}

function xor(a, b){
  return create0(a.l ^ b.l, a.m ^ b.m, a.h ^ b.h);
}

function $clinit_BigLongLib$Const(){
  $clinit_BigLongLib$Const = emptyMethod;
  MAX_VALUE = create0($intern_18, $intern_18, 524287);
  MIN_VALUE = create0(0, 0, $intern_22);
  ONE = create_2(1);
  create_2(2);
  ZERO = create_2(0);
}

var MAX_VALUE, MIN_VALUE, ONE, ZERO;
function toJava(e){
  var javaException;
  if (instanceOf(e, 17)) {
    return e;
  }
  javaException = e && e['__java$exception'];
  if (!javaException) {
    javaException = new JavaScriptException(e);
    captureStackTrace(javaException);
  }
  return javaException;
}

function toJs(t){
  return t.backingJsObject;
}

function compare_5(a, b){
  var result;
  if (isSmallLong0(a) && isSmallLong0(b)) {
    result = a - b;
    if (!isNaN(result)) {
      return result;
    }
  }
  return compare_4(isSmallLong0(a)?toBigLong(a):a, isSmallLong0(b)?toBigLong(b):b);
}

function createLongEmul(big_0){
  var a2;
  a2 = big_0.h;
  if (a2 == 0) {
    return big_0.l + big_0.m * $intern_20;
  }
  if (a2 == $intern_19) {
    return big_0.l + big_0.m * $intern_20 - $intern_21;
  }
  return big_0;
}

function eq(a, b){
  return compare_5(a, b) == 0;
}

function fromDouble_0(value_0){
  if ($intern_23 < value_0 && value_0 < $intern_21) {
    return value_0 < 0?$wnd.Math.ceil(value_0):$wnd.Math.floor(value_0);
  }
  return createLongEmul(fromDouble(value_0));
}

function isSmallLong0(value_0){
  return typeof value_0 === 'number';
}

function shru_0(a, n){
  return createLongEmul(shru(isSmallLong0(a)?toBigLong(a):a, n));
}

function toBigLong(longValue){
  var a0, a1, a3, value_0;
  value_0 = longValue;
  a3 = 0;
  if (value_0 < 0) {
    value_0 += $intern_21;
    a3 = $intern_19;
  }
  a1 = round_int(value_0 / $intern_20);
  a0 = round_int(value_0 - a1 * $intern_20);
  return create0(a0, a1, a3);
}

function toDouble_0(a){
  var d;
  if (isSmallLong0(a)) {
    d = a;
    return d == -0.?0:d;
  }
  return toDouble(a);
}

function toInt(a){
  if (isSmallLong0(a)) {
    return a | 0;
  }
  return a.l | a.m << 22;
}

function xor_0(a, b){
  return createLongEmul(xor(isSmallLong0(a)?toBigLong(a):a, isSmallLong0(b)?toBigLong(b):b));
}

function init_0(){
  $wnd.setTimeout($entry(assertCompileTimeUserAgent));
  $onModuleLoad_1();
  $clinit_LogConfiguration();
  $onModuleLoad_0();
  $onModuleLoad(new BaseletGWT);
}

function $getLevel(this$static){
  if (this$static.level) {
    return this$static.level;
  }
  return $clinit_Level() , ALL;
}

function $setFormatter(this$static, newFormatter){
  this$static.formatter = newFormatter;
}

function $setLevel(this$static, newLevel){
  this$static.level = newLevel;
}

defineClass(168, 1, $intern_24);
var Ljava_util_logging_Handler_2_classLit = createForClass('java.util.logging', 'Handler', 168);
function ConsoleLogHandler(){
  $setFormatter(this, new TextLogFormatter(true));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(389, 168, $intern_24, ConsoleLogHandler);
_.publish = function publish(record){
  var msg, val;
  if (!window.console || ($getLevel(this) , $intern_25 > record.level.intValue())) {
    return;
  }
  msg = $format_0(this.formatter, record);
  val = record.level.intValue();
  val >= ($clinit_Level() , $intern_15)?(window.console.error(msg) , undefined):val >= 900?(window.console.warn(msg) , undefined):val >= 800?(window.console.info(msg) , undefined):(window.console.log(msg) , undefined);
}
;
var Lcom_google_gwt_logging_client_ConsoleLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'ConsoleLogHandler', 389);
function DevelopmentModeLogHandler(){
  $setFormatter(this, new TextLogFormatter(false));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(390, 168, $intern_24, DevelopmentModeLogHandler);
_.publish = function publish_0(record){
  return;
}
;
var Lcom_google_gwt_logging_client_DevelopmentModeLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'DevelopmentModeLogHandler', 390);
function $clinit_LogConfiguration(){
  $clinit_LogConfiguration = emptyMethod;
  impl_1 = new LogConfiguration$LogConfigurationImplRegular;
}

function $onModuleLoad_0(){
  var log_0;
  $configureClientSideLogging(impl_1);
  if (!uncaughtExceptionHandler) {
    log_0 = getLogger(($ensureNamesAreInitialized(Lcom_google_gwt_logging_client_LogConfiguration_2_classLit) , Lcom_google_gwt_logging_client_LogConfiguration_2_classLit.typeName));
    setUncaughtExceptionHandler(new LogConfiguration$1(log_0));
  }
}

var impl_1;
var Lcom_google_gwt_logging_client_LogConfiguration_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration', null);
function LogConfiguration$1(val$log){
  this.val$log2 = val$log;
}

defineClass(388, 1, {}, LogConfiguration$1);
var Lcom_google_gwt_logging_client_LogConfiguration$1_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/1', 388);
function $configureClientSideLogging(this$static){
  this$static.root = getLogger('');
  $setUseParentHandlers(this$static.root);
  $setLevels(this$static.root);
  $setDefaultHandlers(this$static.root);
}

function $setDefaultHandlers(l){
  var console_0, dev, system;
  console_0 = new ConsoleLogHandler;
  $addHandler_1(l, console_0);
  dev = new DevelopmentModeLogHandler;
  $addHandler_1(l, dev);
  system = new SystemLogHandler;
  $addHandler_1(l, system);
}

function $setLevels(l){
  var level, levelParam;
  levelParam = getParameter('logLevel');
  level = levelParam == null?null:parse_0(levelParam);
  level?$setLevel_0(l, level):$setLevel_0(l, ($clinit_Level() , INFO));
}

function LogConfiguration$LogConfigurationImplRegular(){
}

defineClass(387, 1, {}, LogConfiguration$LogConfigurationImplRegular);
var Lcom_google_gwt_logging_client_LogConfiguration$LogConfigurationImplRegular_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/LogConfigurationImplRegular', 387);
function SystemLogHandler(){
  $setFormatter(this, new TextLogFormatter(true));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(391, 168, $intern_24, SystemLogHandler);
_.publish = function publish_1(record){
  return;
}
;
var Lcom_google_gwt_logging_client_SystemLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'SystemLogHandler', 391);
defineClass(1023, 1, {});
var Ljava_util_logging_Formatter_2_classLit = createForClass('java.util.logging', 'Formatter', 1023);
defineClass($intern_26, 1023, {});
var Lcom_google_gwt_logging_impl_FormatterImpl_2_classLit = createForClass('com.google.gwt.logging.impl', 'FormatterImpl', $intern_26);
function $format_0(this$static, event_0){
  var message, date, s;
  message = new StringBuilder;
  $append_5(message, (date = new Date_1(event_0.millis_0) , s = new StringBuilder , $append_5(s, $toString_6(date)) , s.string += ' ' , $append_5(s, event_0.loggerName) , s.string += '\n' , $append_5(s, event_0.level.getName()) , s.string += ': ' , s.string));
  $append_5(message, event_0.msg);
  if (this$static.showStackTraces && !!event_0.thrown) {
    message.string += '\n';
    $printStackTraceImpl(event_0.thrown, new StackTracePrintStream(message), '', '');
  }
  return message.string;
}

function TextLogFormatter(showStackTraces){
  this.showStackTraces = showStackTraces;
}

defineClass(263, $intern_26, {}, TextLogFormatter);
_.showStackTraces = false;
var Lcom_google_gwt_logging_client_TextLogFormatter_2_classLit = createForClass('com.google.gwt.logging.client', 'TextLogFormatter', 263);
defineClass(1013, 1, {});
var Ljava_io_OutputStream_2_classLit = createForClass('java.io', 'OutputStream', 1013);
function FilterOutputStream(out){
}

defineClass(310, 1013, {}, FilterOutputStream);
var Ljava_io_FilterOutputStream_2_classLit = createForClass('java.io', 'FilterOutputStream', 310);
function PrintStream(out){
  FilterOutputStream.call(this, out);
}

defineClass(249, 310, {}, PrintStream);
_.println = function println(s){
}
;
var Ljava_io_PrintStream_2_classLit = createForClass('java.io', 'PrintStream', 249);
function StackTracePrintStream(builder){
  PrintStream.call(this, new FilterOutputStream(null));
  this.builder = builder;
}

defineClass(605, 249, {}, StackTracePrintStream);
_.println = function println_0(str){
  $append_5(this.builder, str);
  $append_5(this.builder, '\n');
}
;
var Lcom_google_gwt_logging_impl_StackTracePrintStream_2_classLit = createForClass('com.google.gwt.logging.impl', 'StackTracePrintStream', 605);
function Storage_0(){
  this.storage = 'localStorage';
}

defineClass(505, 1, {}, Storage_0);
var localStorage_1;
var Lcom_google_gwt_storage_client_Storage_2_classLit = createForClass('com.google.gwt.storage.client', 'Storage', 505);
function $clinit_Storage$StorageSupportDetector(){
  $clinit_Storage$StorageSupportDetector = emptyMethod;
  localStorageSupported = checkStorageSupport('localStorage');
  checkStorageSupport('sessionStorage');
}

function checkStorageSupport(storage){
  var c = '_gwt_dummy_';
  try {
    $wnd[storage].setItem(c, c);
    $wnd[storage].removeItem(c);
    return true;
  }
   catch (e) {
    return false;
  }
}

var localStorageSupported = false;
function $clinit_DOM(){
  $clinit_DOM = emptyMethod;
  $clinit_DOMImplStandard();
}

function dispatchEvent_0(evt, elem){
  $clinit_DOM();
  var eventListener;
  eventListener = getEventListener(elem);
  if (!eventListener) {
    return false;
  }
  dispatchEvent_1(evt, elem, eventListener);
  return true;
}

function dispatchEvent_1(evt, elem, listener){
  $clinit_DOM();
  var prevCurrentEvent;
  prevCurrentEvent = currentEvent;
  currentEvent = evt;
  elem == sCaptureElem && $eventGetTypeInt(evt.type) == 8192 && (sCaptureElem = null);
  listener.onBrowserEvent(evt);
  currentEvent = prevCurrentEvent;
}

function previewEvent(evt){
  $clinit_DOM();
  var ret;
  ret = fire_4(handlers_0, evt);
  if (!ret && !!evt) {
    evt.stopPropagation();
    evt.preventDefault();
  }
  return ret;
}

function setStyleAttribute(elem, value_0){
  $clinit_DOM();
  elem.style['opacity'] = value_0;
}

function sinkEvents(elem, eventBits){
  $clinit_DOM();
  $maybeInitializeEventSystem();
  $sinkEventsImpl(elem, eventBits);
}

var currentEvent = null, sCaptureElem;
function $onModuleLoad_1(){
  var allowedModes, currentMode, i;
  currentMode = $doc.compatMode;
  allowedModes = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['CSS1Compat']);
  for (i = 0; i < allowedModes.length; i++) {
    if ($equals_6(allowedModes[i], currentMode)) {
      return;
    }
  }
  allowedModes.length == 1 && $equals_6('CSS1Compat', allowedModes[0]) && $equals_6('BackCompat', currentMode)?"GWT no longer supports Quirks Mode (document.compatMode=' BackCompat').<br>Make sure your application's host HTML page has a Standards Mode (document.compatMode=' CSS1Compat') doctype,<br>e.g. by using &lt;!doctype html&gt; at the start of your application's HTML page.<br><br>To continue using this unsupported rendering mode and risk layout problems, suppress this message by adding<br>the following line to your*.gwt.xml module file:<br>&nbsp;&nbsp;&lt;extend-configuration-property name=\"document.compatMode\" value=\"" + currentMode + '"/&gt;':"Your *.gwt.xml module configuration prohibits the use of the current document rendering mode (document.compatMode=' " + currentMode + "').<br>Modify your application's host HTML page doctype, or update your custom " + "'document.compatMode' configuration property settings.";
}

var handlers_0;
function fire_4(handlers, nativeEvent){
  var lastIsCanceled, lastIsConsumed, lastIsFirstHandler, lastNativeEvent, ret;
  if (!!TYPE_27 && !!handlers && $isEventHandled(handlers, TYPE_27)) {
    lastIsCanceled = singleton.isCanceled;
    lastIsConsumed = singleton.isConsumed;
    lastIsFirstHandler = singleton.isFirstHandler;
    lastNativeEvent = singleton.nativeEvent;
    singleton.revive();
    singleton.setNativeEvent_0(nativeEvent);
    $fireEvent(handlers, singleton);
    ret = !(singleton.isCanceled_0() && !singleton.isConsumed_0());
    singleton.isCanceled = lastIsCanceled;
    singleton.isConsumed = lastIsConsumed;
    singleton.isFirstHandler = lastIsFirstHandler;
    singleton.nativeEvent = lastNativeEvent;
    return ret;
  }
  return true;
}

var TYPE_27, singleton;
function addCloseHandler(handler){
  maybeInitializeCloseHandlers();
  return addHandler(TYPE_24?TYPE_24:(TYPE_24 = new GwtEvent$Type), handler);
}

function addHandler(type_0, handler){
  return $addHandler_0((!handlers_1 && (handlers_1 = new Window$WindowHandlers) , handlers_1), type_0, handler);
}

function addWindowClosingHandler(handler){
  maybeInitializeCloseHandlers();
  return addHandler(($clinit_Window$ClosingEvent() , $clinit_Window$ClosingEvent() , TYPE_28), handler);
}

function maybeInitializeCloseHandlers(){
  if (!closeHandlersInitialized) {
    $initHandler('function __gwt_initWindowCloseHandler(beforeunload, unload) {\n  var wnd = window\n  , oldOnBeforeUnload = wnd.onbeforeunload\n  , oldOnUnload = wnd.onunload;\n  \n  wnd.onbeforeunload = function(evt) {\n    var ret, oldRet;\n    try {\n      ret = beforeunload();\n    } finally {\n      oldRet = oldOnBeforeUnload && oldOnBeforeUnload(evt);\n    }\n    // Avoid returning null as IE6 will coerce it into a string.\n    // Ensure that "" gets returned properly.\n    if (ret != null) {\n      return ret;\n    }\n    if (oldRet != null) {\n      return oldRet;\n    }\n    // returns undefined.\n  };\n  \n  wnd.onunload = function(evt) {\n    try {\n      unload();\n    } finally {\n      oldOnUnload && oldOnUnload(evt);\n      wnd.onresize = null;\n      wnd.onscroll = null;\n      wnd.onbeforeunload = null;\n      wnd.onunload = null;\n    }\n  };\n  \n  // Remove the reference once we\'ve initialize the handler\n  wnd.__gwt_initWindowCloseHandler = undefined;\n}\n', new WindowImplIE$1);
    closeHandlersInitialized = true;
  }
}

function onClosed(){
  closeHandlersInitialized && fire_1((!handlers_1 && (handlers_1 = new Window$WindowHandlers) , handlers_1));
}

function onClosing(){
  var event_0;
  if (closeHandlersInitialized) {
    event_0 = new Window$ClosingEvent;
    !!handlers_1 && $fireEvent(handlers_1, event_0);
    return event_0.message_0;
  }
  return null;
}

var closeHandlersInitialized = false, handlers_1;
function $clinit_Window$ClosingEvent(){
  $clinit_Window$ClosingEvent = emptyMethod;
  TYPE_28 = new GwtEvent$Type;
}

function Window$ClosingEvent(){
  $clinit_Window$ClosingEvent();
}

defineClass(395, 998, {}, Window$ClosingEvent);
_.dispatch_0 = function dispatch_29(handler){
  var lastArg;
  (lastArg = this , castTo(handler, 997) , lastArg).message_0 = 'Do you really want to leave the page? You will lose any unsaved changes.';
}
;
_.getAssociatedType = function getAssociatedType_29(){
  return TYPE_28;
}
;
_.message_0 = null;
var TYPE_28;
var Lcom_google_gwt_user_client_Window$ClosingEvent_2_classLit = createForClass('com.google.gwt.user.client', 'Window/ClosingEvent', 395);
function buildListParamMap(queryString){
  var entry, entry$iterator, key, kv, kvPair, kvPair$array, kvPair$index, kvPair$max, out, qs, val, values, regexp;
  out = new HashMap;
  if (queryString != null && queryString.length > 1) {
    qs = queryString.substr(1);
    for (kvPair$array = $split(qs, '&', 0) , kvPair$index = 0 , kvPair$max = kvPair$array.length; kvPair$index < kvPair$max; ++kvPair$index) {
      kvPair = kvPair$array[kvPair$index];
      kv = $split(kvPair, '=', 2);
      key = kv[0];
      if (key.length == 0) {
        continue;
      }
      val = kv.length > 1?kv[1]:'';
      try {
        val = (throwIfNull('encodedURLComponent', val) , regexp = /\+/g , decodeURIComponent(val.replace(regexp, '%20')));
      }
       catch ($e0) {
        $e0 = toJava($e0);
        if (!instanceOf($e0, 99))
          throw toJs($e0);
      }
      values = castTo(out.get_1(key), 40);
      if (!values) {
        values = new ArrayList;
        out.put(key, values);
      }
      values.add_0(val);
    }
  }
  for (entry$iterator = out.entrySet_0().iterator(); entry$iterator.hasNext_0();) {
    entry = castTo(entry$iterator.next_1(), 44);
    entry.setValue(unmodifiableList(castTo(entry.getValue_0(), 40)));
  }
  out = ($clinit_Collections() , new Collections$UnmodifiableMap(out));
  return out;
}

function ensureListParameterMap(){
  var currentQueryString, href_0, hashLoc, questionLoc;
  currentQueryString = (href_0 = $wnd.location.href , hashLoc = href_0.indexOf('#') , hashLoc >= 0 && (href_0 = href_0.substring(0, hashLoc)) , questionLoc = href_0.indexOf('?') , questionLoc > 0?href_0.substring(questionLoc):'');
  if (!listParamMap || !$equals_6(cachedQueryString, currentQueryString)) {
    listParamMap = buildListParamMap(currentQueryString);
    cachedQueryString = currentQueryString;
  }
}

function getParameter(name_0){
  var paramsForName;
  ensureListParameterMap();
  paramsForName = castTo(listParamMap.get_1(name_0), 40);
  return !paramsForName?null:castToString(paramsForName.get_0(paramsForName.size_1() - 1));
}

var cachedQueryString = '', listParamMap;
function Window$WindowHandlers(){
  HandlerManager.call(this, null);
}

defineClass(246, 165, $intern_16, Window$WindowHandlers);
var Lcom_google_gwt_user_client_Window$WindowHandlers_2_classLit = createForClass('com.google.gwt.user.client', 'Window/WindowHandlers', 246);
function $eventGetTypeInt(eventType){
  switch (eventType) {
    case 'blur':
      return $intern_27;
    case 'change':
      return $intern_26;
    case 'click':
      return 1;
    case 'dblclick':
      return 2;
    case 'focus':
      return $intern_28;
    case 'keydown':
      return 128;
    case 'keypress':
      return 256;
    case 'keyup':
      return 512;
    case 'load':
      return $intern_29;
    case 'losecapture':
      return 8192;
    case 'mousedown':
      return 4;
    case 'mousemove':
      return 64;
    case 'mouseout':
      return 32;
    case 'mouseover':
      return 16;
    case 'mouseup':
      return 8;
    case 'scroll':
      return $intern_30;
    case 'error':
      return $intern_31;
    case 'DOMMouseScroll':
    case 'mousewheel':
      return $intern_32;
    case 'contextmenu':
      return $intern_33;
    case 'paste':
      return $intern_22;
    case 'touchstart':
      return $intern_34;
    case 'touchmove':
      return $intern_35;
    case 'touchend':
      return $intern_20;
    case 'touchcancel':
      return $intern_36;
    case 'gesturestart':
      return $intern_37;
    case 'gesturechange':
      return $intern_38;
    case 'gestureend':
      return $intern_39;
    default:return -1;
  }
}

function $maybeInitializeEventSystem(){
  if (!eventSystemIsInitialized) {
    $initEventSystem();
    eventSystemIsInitialized = true;
  }
}

function getEventListener(elem){
  var maybeListener = elem.__listener;
  return !instanceOfJso(maybeListener) && instanceOf(maybeListener, 29)?maybeListener:null;
}

function setEventListener(elem, listener){
  elem.__listener = listener;
}

var eventSystemIsInitialized = false;
function $clinit_DOMImplStandard(){
  $clinit_DOMImplStandard = emptyMethod;
  bitlessEventDispatchers = {_default_:dispatchEvent_3, dragenter:dispatchDragEvent, dragover:dispatchDragEvent};
  captureEventDispatchers = {click:dispatchCapturedMouseEvent, dblclick:dispatchCapturedMouseEvent, mousedown:dispatchCapturedMouseEvent, mouseup:dispatchCapturedMouseEvent, mousemove:dispatchCapturedMouseEvent, mouseover:dispatchCapturedMouseEvent, mouseout:dispatchCapturedMouseEvent, mousewheel:dispatchCapturedMouseEvent, keydown:dispatchCapturedEvent, keyup:dispatchCapturedEvent, keypress:dispatchCapturedEvent, touchstart:dispatchCapturedMouseEvent, touchend:dispatchCapturedMouseEvent, touchmove:dispatchCapturedMouseEvent, touchcancel:dispatchCapturedMouseEvent, gesturestart:dispatchCapturedMouseEvent, gestureend:dispatchCapturedMouseEvent, gesturechange:dispatchCapturedMouseEvent};
}

function $initEventSystem(){
  dispatchEvent_2 = $entry(dispatchEvent_3);
  dispatchUnhandledEvent = $entry(dispatchUnhandledEvent_0);
  var foreach = foreach_0;
  var bitlessEvents = bitlessEventDispatchers;
  foreach(bitlessEvents, function(e, fn){
    bitlessEvents[e] = $entry(fn);
  }
  );
  var captureEvents_0 = captureEventDispatchers;
  foreach(captureEvents_0, function(e, fn){
    captureEvents_0[e] = $entry(fn);
  }
  );
  foreach(captureEvents_0, function(e, fn){
    $wnd.addEventListener(e, fn, true);
  }
  );
}

function $sinkEventsImpl(elem, bits){
  var chMask = (elem.__eventBits || 0) ^ bits;
  elem.__eventBits = bits;
  if (!chMask)
    return;
  chMask & 1 && (elem.onclick = bits & 1?dispatchEvent_2:null);
  chMask & 2 && (elem.ondblclick = bits & 2?dispatchEvent_2:null);
  chMask & 4 && (elem.onmousedown = bits & 4?dispatchEvent_2:null);
  chMask & 8 && (elem.onmouseup = bits & 8?dispatchEvent_2:null);
  chMask & 16 && (elem.onmouseover = bits & 16?dispatchEvent_2:null);
  chMask & 32 && (elem.onmouseout = bits & 32?dispatchEvent_2:null);
  chMask & 64 && (elem.onmousemove = bits & 64?dispatchEvent_2:null);
  chMask & 128 && (elem.onkeydown = bits & 128?dispatchEvent_2:null);
  chMask & 256 && (elem.onkeypress = bits & 256?dispatchEvent_2:null);
  chMask & 512 && (elem.onkeyup = bits & 512?dispatchEvent_2:null);
  chMask & $intern_26 && (elem.onchange = bits & $intern_26?dispatchEvent_2:null);
  chMask & $intern_28 && (elem.onfocus = bits & $intern_28?dispatchEvent_2:null);
  chMask & $intern_27 && (elem.onblur = bits & $intern_27?dispatchEvent_2:null);
  chMask & 8192 && (elem.onlosecapture = bits & 8192?dispatchEvent_2:null);
  chMask & $intern_30 && (elem.onscroll = bits & $intern_30?dispatchEvent_2:null);
  chMask & $intern_29 && (elem.onload = bits & $intern_29?dispatchUnhandledEvent:null);
  chMask & $intern_31 && (elem.onerror = bits & $intern_31?dispatchEvent_2:null);
  chMask & $intern_32 && (elem.onmousewheel = bits & $intern_32?dispatchEvent_2:null);
  chMask & $intern_33 && (elem.oncontextmenu = bits & $intern_33?dispatchEvent_2:null);
  chMask & $intern_22 && (elem.onpaste = bits & $intern_22?dispatchEvent_2:null);
  chMask & $intern_34 && (elem.ontouchstart = bits & $intern_34?dispatchEvent_2:null);
  chMask & $intern_35 && (elem.ontouchmove = bits & $intern_35?dispatchEvent_2:null);
  chMask & $intern_20 && (elem.ontouchend = bits & $intern_20?dispatchEvent_2:null);
  chMask & $intern_36 && (elem.ontouchcancel = bits & $intern_36?dispatchEvent_2:null);
  chMask & $intern_37 && (elem.ongesturestart = bits & $intern_37?dispatchEvent_2:null);
  chMask & $intern_38 && (elem.ongesturechange = bits & $intern_38?dispatchEvent_2:null);
  chMask & $intern_39 && (elem.ongestureend = bits & $intern_39?dispatchEvent_2:null);
}

function dispatchCapturedEvent(evt){
  previewEvent(evt);
}

function dispatchCapturedMouseEvent(evt){
  var cancelled;
  cancelled = !previewEvent(evt);
  if (cancelled || !captureElem) {
    return;
  }
  dispatchEvent_0(evt, captureElem) && (evt.stopPropagation() , undefined);
}

function dispatchDragEvent(evt){
  evt.preventDefault();
  dispatchEvent_3(evt);
}

function dispatchEvent_3(evt){
  var element;
  element = getFirstAncestorWithListener(evt);
  if (!element) {
    return;
  }
  dispatchEvent_1(evt, element.nodeType != 1?null:element, getEventListener(element));
}

function dispatchUnhandledEvent_0(evt){
  var element;
  element = evt.currentTarget || $wnd;
  element['__gwtLastUnhandledEvent'] = evt.type;
  dispatchEvent_3(evt);
}

function getFirstAncestorWithListener(evt){
  var curElem;
  curElem = evt.currentTarget || $wnd;
  while (!!curElem && !getEventListener(curElem)) {
    curElem = curElem.parentNode;
  }
  return curElem;
}

var bitlessEventDispatchers, captureElem, captureEventDispatchers, dispatchEvent_2, dispatchUnhandledEvent;
function foreach_0(map_0, fn){
  for (var e in map_0) {
    map_0.hasOwnProperty(e) && fn(e, map_0[e]);
  }
}

function $initHandler(initFunc, cmd){
  var scriptElem;
  scriptElem = $createScriptElement($doc, initFunc);
  $appendChild($doc.body, scriptElem);
  cmd.execute();
  $removeChild($doc.body, scriptElem);
}

function WindowImplIE$1(){
}

defineClass(396, 1, {}, WindowImplIE$1);
_.execute = function execute_26(){
  $wnd.__gwt_initWindowCloseHandler($entry(onClosing), $entry(onClosed));
}
;
var Lcom_google_gwt_user_client_impl_WindowImplIE$1_2_classLit = createForClass('com.google.gwt.user.client.impl', 'WindowImplIE/1', 396);
function $remove_5(this$static, w){
  var removed;
  removed = $remove_0(this$static, w);
  removed && changeToStaticPositioning(w.getElement());
  return removed;
}

function changeToStaticPositioning(elem){
  elem.style['left'] = '';
  elem.style['top'] = '';
  elem.style['position'] = '';
}

defineClass(426, 191, $intern_9);
_.remove_1 = function remove_10(w){
  return $remove_5(this, w);
}
;
var Lcom_google_gwt_user_client_ui_AbsolutePanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AbsolutePanel', 426);
function $clinit_AttachDetachException(){
  $clinit_AttachDetachException = emptyMethod;
  attachCommand = new AttachDetachException$1;
  detachCommand = new AttachDetachException$2;
}

function AttachDetachException(causes){
  UmbrellaException_0.call(this, causes);
}

function tryCommand(hasWidgets, c){
  $clinit_AttachDetachException();
  var caught, e, w, w$iterator;
  caught = null;
  for (w$iterator = hasWidgets.iterator(); w$iterator.hasNext_0();) {
    w = castTo(w$iterator.next_1(), 24);
    try {
      c.execute_2(w);
    }
     catch ($e0) {
      $e0 = toJava($e0);
      if (instanceOf($e0, 17)) {
        e = $e0;
        !caught && (caught = new HashSet);
        $put(caught.map_0, e, caught);
      }
       else 
        throw toJs($e0);
    }
  }
  if (caught) {
    throw toJs(new AttachDetachException(caught));
  }
}

defineClass(430, 217, $intern_17, AttachDetachException);
var attachCommand, detachCommand;
var Lcom_google_gwt_user_client_ui_AttachDetachException_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException', 430);
function AttachDetachException$1(){
}

defineClass(431, 1, {}, AttachDetachException$1);
_.execute_2 = function execute_28(w){
  w.onAttach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/1', 431);
function AttachDetachException$2(){
}

defineClass(432, 1, {}, AttachDetachException$2);
_.execute_2 = function execute_29(w){
  w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/2', 432);
function $clinit_RootPanel(){
  $clinit_RootPanel = emptyMethod;
  maybeDetachCommand = new RootPanel$1;
  rootPanels = new HashMap;
  widgetsToDetach = new HashSet;
}

function RootPanel(elem){
  ComplexPanel.call(this);
  $setElement_0(this, ($clinit_DOM() , elem));
  $onAttach(this);
}

function detachWidgets(){
  $clinit_RootPanel();
  try {
    tryCommand(widgetsToDetach, maybeDetachCommand);
  }
   finally {
    $reset_0(widgetsToDetach.map_0);
    $reset_0(rootPanels);
  }
}

function get_6(id_0){
  $clinit_RootPanel();
  var elem, rp;
  rp = castTo($getStringValue(rootPanels, id_0), 192);
  elem = null;
  if (id_0 != null) {
    if (!(elem = $getElementById($doc, id_0))) {
      return null;
    }
  }
  if (rp) {
    if (!elem || ($clinit_DOM() , rp.element == elem)) {
      return rp;
    }
  }
  if ($size(rootPanels) == 0) {
    addCloseHandler(new RootPanel$2);
    $clinit_LocaleInfo();
  }
  !elem?(rp = new RootPanel$DefaultRootPanel):(rp = new RootPanel(elem));
  $putStringValue(rootPanels, id_0, rp);
  $add_14(widgetsToDetach, rp);
  return rp;
}

defineClass(192, 426, $intern_40, RootPanel);
var maybeDetachCommand, rootPanels, widgetsToDetach;
var Lcom_google_gwt_user_client_ui_RootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel', 192);
function RootPanel$1(){
}

defineClass(428, 1, {}, RootPanel$1);
_.execute_2 = function execute_33(w){
  w.isAttached() && w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_RootPanel$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/1', 428);
function RootPanel$2(){
}

defineClass(429, 1, $intern_41, RootPanel$2);
_.onClose = function onClose_0(closeEvent){
  detachWidgets();
}
;
var Lcom_google_gwt_user_client_ui_RootPanel$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/2', 429);
function RootPanel$DefaultRootPanel(){
  RootPanel.call(this, $doc.body);
}

defineClass(427, 192, $intern_40, RootPanel$DefaultRootPanel);
var Lcom_google_gwt_user_client_ui_RootPanel$DefaultRootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/DefaultRootPanel', 427);
function $indexOf(this$static, w){
  var i;
  for (i = 0; i < this$static.size_0; ++i) {
    if (this$static.array[i] == w) {
      return i;
    }
  }
  return -1;
}

function $remove_7(this$static, index_0){
  var i;
  if (index_0 < 0 || index_0 >= this$static.size_0) {
    throw toJs(new IndexOutOfBoundsException);
  }
  --this$static.size_0;
  for (i = index_0; i < this$static.size_0; ++i) {
    this$static.array[i] = this$static.array[i + 1];
  }
  this$static.array[this$static.size_0] = null;
}

function $remove_8(this$static, w){
  var index_0;
  index_0 = $indexOf(this$static, w);
  if (index_0 == -1) {
    throw toJs(new NoSuchElementException);
  }
  $remove_7(this$static, index_0);
}

function WidgetCollection(parent_0){
  this.parent_0 = parent_0;
  this.array = initUnidimensionalArray(Lcom_google_gwt_user_client_ui_Widget_2_classLit, $intern_13, 24, 4, 0, 1);
}

defineClass(610, 1, $intern_42, WidgetCollection);
_.iterator = function iterator_4(){
  return new WidgetCollection$WidgetIterator(this);
}
;
_.size_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection', 610);
function $next_0(this$static){
  if (this$static.index_0 >= this$static.this$01.size_0) {
    throw toJs(new NoSuchElementException);
  }
  this$static.currentWidget = this$static.this$01.array[this$static.index_0];
  ++this$static.index_0;
  return this$static.currentWidget;
}

function WidgetCollection$WidgetIterator(this$0){
  this.this$01 = this$0;
}

defineClass(198, 1, {}, WidgetCollection$WidgetIterator);
_.next_1 = function next_1(){
  return $next_0(this);
}
;
_.hasNext_0 = function hasNext_0(){
  return this.index_0 < this.this$01.size_0;
}
;
_.remove_3 = function remove_16(){
  if (!this.currentWidget) {
    throw toJs(new IllegalStateException);
  }
  this.this$01.parent_0.remove_1(this.currentWidget);
  --this.index_0;
  this.currentWidget = null;
}
;
_.index_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection$WidgetIterator_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection/WidgetIterator', 198);
function assertCompileTimeUserAgent(){
  var runtimeValue;
  runtimeValue = $getRuntimeValue();
  if (!$equals_6('ie10', runtimeValue)) {
    throw toJs(new UserAgentAsserter$UserAgentAssertionError(runtimeValue));
  }
}

function Error_0(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(245, 17, $intern_4);
var Ljava_lang_Error_2_classLit = createForClass('java.lang', 'Error', 245);
defineClass(89, 245, $intern_4);
var Ljava_lang_AssertionError_2_classLit = createForClass('java.lang', 'AssertionError', 89);
function UserAgentAsserter$UserAgentAssertionError(runtimeValue){
  Error_0.call(this, 'Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (ie10) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.' == null?'null':toString_36('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (ie10) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.'), instanceOf('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (ie10) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 17)?castTo('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (ie10) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 17):null);
}

defineClass(386, 89, $intern_4, UserAgentAsserter$UserAgentAssertionError);
var Lcom_google_gwt_useragent_client_UserAgentAsserter$UserAgentAssertionError_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentAsserter/UserAgentAssertionError', 386);
function $getRuntimeValue(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}

function SimpleEventBus$1(this$0, val$type, val$source, val$handler){
  this.this$01 = this$0;
  this.val$type2 = val$type;
  this.val$source3 = val$source;
  this.val$handler4 = val$handler;
}

defineClass(401, 1, {}, SimpleEventBus$1);
var Lcom_google_web_bindery_event_shared_SimpleEventBus$1_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/1', 401);
function SimpleEventBus$2(this$0, val$type, val$source, val$handler){
  this.this$01 = this$0;
  this.val$type2 = val$type;
  this.val$source3 = val$source;
  this.val$handler4 = val$handler;
}

defineClass(400, 1, {402:1}, SimpleEventBus$2);
_.execute = function execute_36(){
  $doAddNow(this.this$01, this.val$type2, this.val$source3, this.val$handler4);
}
;
var Lcom_google_web_bindery_event_shared_SimpleEventBus$2_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/2', 400);
function AbstractStringBuilder(string){
  this.string = string;
}

defineClass(219, 1, $intern_43);
_.toString_0 = function toString_46(){
  return this.string;
}
;
var Ljava_lang_AbstractStringBuilder_2_classLit = createForClass('java.lang', 'AbstractStringBuilder', 219);
function IndexOutOfBoundsException(){
  RuntimeException.call(this);
}

function IndexOutOfBoundsException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(64, 10, $intern_44, IndexOutOfBoundsException, IndexOutOfBoundsException_0);
var Ljava_lang_IndexOutOfBoundsException_2_classLit = createForClass('java.lang', 'IndexOutOfBoundsException', 64);
function ArrayStoreException(){
  RuntimeException.call(this);
}

defineClass(311, 10, $intern_5, ArrayStoreException);
var Ljava_lang_ArrayStoreException_2_classLit = createForClass('java.lang', 'ArrayStoreException', 311);
function $clinit_Boolean(){
  $clinit_Boolean = emptyMethod;
  FALSE = false;
}

booleanCastMap = {3:1, 305:1, 9:1};
var FALSE;
var Ljava_lang_Boolean_2_classLit = createForClass('java.lang', 'Boolean', 305);
function ClassCastException(){
  RuntimeException_0.call(this, null);
}

defineClass(411, 10, $intern_5, ClassCastException);
var Ljava_lang_ClassCastException_2_classLit = createForClass('java.lang', 'ClassCastException', 411);
defineClass(218, 1, {3:1, 218:1});
var Ljava_lang_Number_2_classLit = createForClass('java.lang', 'Number', 218);
function $equals_4(this$static, o){
  return checkCriticalNotNull(this$static) , this$static === o;
}

function $hashCode_3(this$static){
  return round_int((checkCriticalNotNull(this$static) , this$static));
}

doubleCastMap = {3:1, 9:1, 248:1, 218:1};
var Ljava_lang_Double_2_classLit = createForClass('java.lang', 'Double', 248);
function IllegalArgumentException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(71, 10, $intern_5, IllegalArgumentException_0);
var Ljava_lang_IllegalArgumentException_2_classLit = createForClass('java.lang', 'IllegalArgumentException', 71);
function IllegalStateException(){
  RuntimeException.call(this);
}

function IllegalStateException_0(s){
  RuntimeException_0.call(this, s);
}

defineClass(82, 10, $intern_5, IllegalStateException, IllegalStateException_0);
var Ljava_lang_IllegalStateException_2_classLit = createForClass('java.lang', 'IllegalStateException', 82);
function $equals_5(this$static, o){
  return instanceOf(o, 45) && castTo(o, 45).value_0 == this$static.value_0;
}

function Integer(value_0){
  this.value_0 = value_0;
}

function valueOf_9(i){
  var rebase, result;
  if (i > -129 && i < 128) {
    rebase = i + 128;
    result = ($clinit_Integer$BoxedValues() , boxedValues)[rebase];
    !result && (result = boxedValues[rebase] = new Integer(i));
    return result;
  }
  return new Integer(i);
}

defineClass(45, 218, $intern_45, Integer);
_.equals_0 = function equals_25(o){
  return $equals_5(this, o);
}
;
_.hashCode_0 = function hashCode_22(){
  return this.value_0;
}
;
_.toString_0 = function toString_49(){
  return '' + this.value_0;
}
;
_.value_0 = 0;
var Ljava_lang_Integer_2_classLit = createForClass('java.lang', 'Integer', 45);
function $clinit_Integer$BoxedValues(){
  $clinit_Integer$BoxedValues = emptyMethod;
  boxedValues = initUnidimensionalArray(Ljava_lang_Integer_2_classLit, {3:1, 6:1, 7:1, 241:1, 4:1}, 45, 256, 0, 1);
}

var boxedValues;
defineClass(1094, 1, {});
function NullPointerException(){
  RuntimeException.call(this);
}

function NullPointerException_0(typeError){
  JsException.call(this, typeError);
}

function NullPointerException_1(message){
  RuntimeException_0.call(this, message);
}

defineClass(98, 189, $intern_5, NullPointerException, NullPointerException_0, NullPointerException_1);
_.createError = function createError_0(msg){
  return new TypeError(msg);
}
;
var Ljava_lang_NullPointerException_2_classLit = createForClass('java.lang', 'NullPointerException', 98);
function StackTraceElement(methodName, fileName, lineNumber){
  this.className_0 = 'Unknown';
  this.methodName = methodName;
  this.fileName = fileName;
  this.lineNumber = lineNumber;
}

defineClass(121, 1, {3:1, 121:1}, StackTraceElement);
_.equals_0 = function equals_26(other){
  var st;
  if (instanceOf(other, 121)) {
    st = castTo(other, 121);
    return this.lineNumber == st.lineNumber && this.methodName == st.methodName && this.className_0 == st.className_0 && this.fileName == st.fileName;
  }
  return false;
}
;
_.hashCode_0 = function hashCode_23(){
  return hashCode_28(stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_13, 1, 5, [valueOf_9(this.lineNumber), this.className_0, this.methodName, this.fileName]));
}
;
_.toString_0 = function toString_50(){
  return this.className_0 + '.' + this.methodName + '(' + (this.fileName != null?this.fileName:'Unknown Source') + (this.lineNumber >= 0?':' + this.lineNumber:'') + ')';
}
;
_.lineNumber = 0;
var Ljava_lang_StackTraceElement_2_classLit = createForClass('java.lang', 'StackTraceElement', 121);
function $charAt(this$static, index_0){
  checkCriticalStringElementIndex(index_0, this$static.length);
  return this$static.charCodeAt(index_0);
}

function $equals_6(this$static, other){
  return checkCriticalNotNull(this$static) , this$static === other;
}

function $equalsIgnoreCase(this$static, other){
  checkCriticalNotNull(this$static);
  if (other == null) {
    return false;
  }
  if ($equals_6(this$static, other)) {
    return true;
  }
  return this$static.length == other.length && $equals_6(this$static.toLowerCase(), other.toLowerCase());
}

function $indexOf_0(this$static, str){
  return this$static.indexOf(str);
}

function $lastIndexOf(this$static, str){
  return this$static.lastIndexOf(str);
}

function $lastIndexOf_0(this$static, str, start_0){
  return this$static.lastIndexOf(str, start_0);
}

function $split(this$static, regex, maxMatch){
  var compiled, count, lastNonEmpty, lastTrail, matchIndex, matchObj, out, trail;
  compiled = new RegExp(regex, 'g');
  out = initUnidimensionalArray(Ljava_lang_String_2_classLit, $intern_6, 2, 0, 6, 1);
  count = 0;
  trail = this$static;
  lastTrail = null;
  while (true) {
    matchObj = compiled.exec(trail);
    if (matchObj == null || trail == '' || count == maxMatch - 1 && maxMatch > 0) {
      out[count] = trail;
      break;
    }
     else {
      matchIndex = matchObj.index;
      out[count] = trail.substr(0, matchIndex);
      trail = $substring_0(trail, matchIndex + matchObj[0].length, trail.length);
      compiled.lastIndex = 0;
      if (lastTrail == trail) {
        out[count] = trail.substr(0, 1);
        trail = trail.substr(1);
      }
      lastTrail = trail;
      ++count;
    }
  }
  if (maxMatch == 0 && this$static.length > 0) {
    lastNonEmpty = out.length;
    while (lastNonEmpty > 0 && out[lastNonEmpty - 1] == '') {
      --lastNonEmpty;
    }
    lastNonEmpty < out.length && (out.length = lastNonEmpty);
  }
  return out;
}

function $startsWith(this$static, prefix){
  return $equals_6(this$static.substr(0, prefix.length), prefix);
}

function $substring_0(this$static, beginIndex, endIndex){
  return this$static.substr(beginIndex, endIndex - beginIndex);
}

function $toUpperCase(this$static, locale){
  return locale == ($clinit_Locale() , $clinit_Locale() , defaultLocale)?this$static.toLocaleUpperCase():this$static.toUpperCase();
}

function $trim(this$static){
  var end, length_0, start_0;
  length_0 = this$static.length;
  start_0 = 0;
  while (start_0 < length_0 && (checkCriticalStringElementIndex(start_0, this$static.length) , this$static.charCodeAt(start_0) <= 32)) {
    ++start_0;
  }
  end = length_0;
  while (end > start_0 && (checkCriticalStringElementIndex(end - 1, this$static.length) , this$static.charCodeAt(end - 1) <= 32)) {
    --end;
  }
  return start_0 > 0 || end < length_0?this$static.substr(start_0, end - start_0):this$static;
}

function fromCodePoint(codePoint){
  var hiSurrogate, loSurrogate;
  if (codePoint >= $intern_31) {
    hiSurrogate = 55296 + (codePoint - $intern_31 >> 10 & 1023) & $intern_46;
    loSurrogate = 56320 + (codePoint - $intern_31 & 1023) & $intern_46;
    return String.fromCharCode(hiSurrogate) + ('' + String.fromCharCode(loSurrogate));
  }
   else {
    return String.fromCharCode(codePoint & $intern_46);
  }
}

stringCastMap = {3:1, 298:1, 9:1, 2:1};
var Ljava_lang_String_2_classLit = createForClass('java.lang', 'String', 2);
function $append_3(this$static, x_0){
  this$static.string += '' + x_0;
  return this$static;
}

function $append_5(this$static, x_0){
  this$static.string += '' + x_0;
  return this$static;
}

function StringBuilder(){
  AbstractStringBuilder.call(this, '');
}

function StringBuilder_1(s){
  AbstractStringBuilder.call(this, (checkCriticalNotNull(s) , s));
}

defineClass(43, 219, $intern_43, StringBuilder, StringBuilder_1);
var Ljava_lang_StringBuilder_2_classLit = createForClass('java.lang', 'StringBuilder', 43);
function StringIndexOutOfBoundsException(message){
  IndexOutOfBoundsException_0.call(this, message);
}

defineClass(308, 64, $intern_44, StringIndexOutOfBoundsException);
var Ljava_lang_StringIndexOutOfBoundsException_2_classLit = createForClass('java.lang', 'StringIndexOutOfBoundsException', 308);
function $clinit_System(){
  $clinit_System = emptyMethod;
  err = new PrintStream(null);
  new PrintStream(null);
}

defineClass(1098, 1, {});
var err;
function UnsupportedOperationException(){
  RuntimeException.call(this);
}

function UnsupportedOperationException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(63, 10, $intern_5, UnsupportedOperationException, UnsupportedOperationException_0);
var Ljava_lang_UnsupportedOperationException_2_classLit = createForClass('java.lang', 'UnsupportedOperationException', 63);
function $advanceToFind(this$static, o, remove){
  var e, iter;
  for (iter = this$static.iterator(); iter.hasNext_0();) {
    e = iter.next_1();
    if (maskUndefined(o) === maskUndefined(e) || o != null && equals_Ljava_lang_Object__Z__devirtual$(o, e)) {
      remove && iter.remove_3();
      return true;
    }
  }
  return false;
}

function $containsAll(this$static, c){
  var e, e$iterator;
  checkCriticalNotNull(c);
  for (e$iterator = c.iterator(); e$iterator.hasNext_0();) {
    e = e$iterator.next_1();
    if (!this$static.contains_0(e)) {
      return false;
    }
  }
  return true;
}

function $toArray(this$static, a){
  var i, it, size_0;
  size_0 = this$static.size_1();
  a.length < size_0 && (a = stampJavaTypeInfo_1(new Array(size_0), a));
  it = this$static.iterator();
  for (i = 0; i < size_0; ++i) {
    setCheck(a, i, it.next_1());
  }
  a.length > size_0 && setCheck(a, size_0, null);
  return a;
}

function $toString_5(this$static){
  var e, e$iterator, joiner;
  joiner = new StringJoiner('[', ']');
  for (e$iterator = this$static.iterator(); e$iterator.hasNext_0();) {
    e = e$iterator.next_1();
    $add_17(joiner, e === this$static?'(this Collection)':e == null?'null':toString_36(e));
  }
  return !joiner.builder?joiner.emptyValue:joiner.suffix.length == 0?joiner.builder.string:joiner.builder.string + ('' + joiner.suffix);
}

defineClass(1015, 1, $intern_47);
_.add_0 = function add_4(o){
  throw toJs(new UnsupportedOperationException_0('Add not supported on this collection'));
}
;
_.contains_0 = function contains_0(o){
  return $advanceToFind(this, o, false);
}
;
_.isEmpty = function isEmpty_1(){
  return this.size_1() == 0;
}
;
_.toArray = function toArray_1(){
  return this.toArray_0(initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_13, 1, this.size_1(), 5, 1));
}
;
_.toArray_0 = function toArray_2(a){
  return $toArray(this, a);
}
;
_.toString_0 = function toString_51(){
  return $toString_5(this);
}
;
var Ljava_util_AbstractCollection_2_classLit = createForClass('java.util', 'AbstractCollection', 1015);
defineClass($intern_48, 1015, $intern_49);
_.equals_0 = function equals_27(o){
  var other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 104)) {
    return false;
  }
  other = castTo(o, 104);
  if (other.size_1() != this.size_1()) {
    return false;
  }
  return $containsAll(this, other);
}
;
_.hashCode_0 = function hashCode_24(){
  return hashCode_29(this);
}
;
var Ljava_util_AbstractSet_2_classLit = createForClass('java.util', 'AbstractSet', $intern_48);
function $contains_3(this$static, o){
  if (instanceOf(o, 44)) {
    return $containsEntry(this$static.this$01, castTo(o, 44));
  }
  return false;
}

function AbstractHashMap$EntrySet(this$0){
  this.this$01 = this$0;
}

defineClass(66, $intern_48, $intern_49, AbstractHashMap$EntrySet);
_.contains_0 = function contains_1(o){
  return $contains_3(this, o);
}
;
_.iterator = function iterator_5(){
  return new AbstractHashMap$EntrySetIterator(this.this$01);
}
;
_.size_1 = function size_5(){
  return this.this$01.size_1();
}
;
var Ljava_util_AbstractHashMap$EntrySet_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySet', 66);
function $computeHasNext(this$static){
  if (this$static.current.hasNext_0()) {
    return true;
  }
  if (this$static.current != this$static.stringMapEntries) {
    return false;
  }
  this$static.current = new InternalHashCodeMap$1(this$static.this$01.hashCodeMap);
  return this$static.current.hasNext_0();
}

function $next_1(this$static){
  var rv;
  checkStructuralChange(this$static.this$01, this$static);
  checkCriticalElement(this$static.hasNext);
  this$static.last = this$static.current;
  rv = castTo(this$static.current.next_1(), 44);
  this$static.hasNext = $computeHasNext(this$static);
  return rv;
}

function $remove_9(this$static){
  checkCriticalState(!!this$static.last);
  checkStructuralChange(this$static.this$01, this$static);
  this$static.last.remove_3();
  this$static.last = null;
  this$static.hasNext = $computeHasNext(this$static);
  recordLastKnownStructure(this$static.this$01, this$static);
}

function AbstractHashMap$EntrySetIterator(this$0){
  this.this$01 = this$0;
  this.stringMapEntries = new InternalStringMap$1(this.this$01.stringMap);
  this.current = this.stringMapEntries;
  this.hasNext = $computeHasNext(this);
  this.$modCount = this$0.$modCount;
}

defineClass(67, 1, {}, AbstractHashMap$EntrySetIterator);
_.next_1 = function next_2(){
  return $next_1(this);
}
;
_.hasNext_0 = function hasNext_1(){
  return this.hasNext;
}
;
_.remove_3 = function remove_19(){
  $remove_9(this);
}
;
_.hasNext = false;
var Ljava_util_AbstractHashMap$EntrySetIterator_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySetIterator', 67);
defineClass($intern_50, 1015, $intern_51);
_.add_1 = function add_5(index_0, element){
  throw toJs(new UnsupportedOperationException_0('Add not supported on this list'));
}
;
_.add_0 = function add_6(obj){
  this.add_1(this.size_1(), obj);
  return true;
}
;
_.equals_0 = function equals_28(o){
  var elem, elem$iterator, elemOther, iterOther, other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 40)) {
    return false;
  }
  other = castTo(o, 40);
  if (this.size_1() != other.size_1()) {
    return false;
  }
  iterOther = other.iterator();
  for (elem$iterator = this.iterator(); elem$iterator.hasNext_0();) {
    elem = elem$iterator.next_1();
    elemOther = iterOther.next_1();
    if (!(maskUndefined(elem) === maskUndefined(elemOther) || elem != null && equals_Ljava_lang_Object__Z__devirtual$(elem, elemOther))) {
      return false;
    }
  }
  return true;
}
;
_.hashCode_0 = function hashCode_25(){
  return hashCode_30(this);
}
;
_.iterator = function iterator_6(){
  return new AbstractList$IteratorImpl(this);
}
;
_.listIterator = function listIterator_1(){
  return this.listIterator_0(0);
}
;
_.listIterator_0 = function listIterator_2(from){
  return new AbstractList$ListIteratorImpl(this, from);
}
;
_.remove = function remove_20(index_0){
  throw toJs(new UnsupportedOperationException_0('Remove not supported on this list'));
}
;
var Ljava_util_AbstractList_2_classLit = createForClass('java.util', 'AbstractList', $intern_50);
function $hasNext(this$static){
  return this$static.i < this$static.this$01_0.size_1();
}

function $next_2(this$static){
  checkCriticalElement(this$static.i < this$static.this$01_0.size_1());
  return this$static.this$01_0.get_0(this$static.last = this$static.i++);
}

function $remove_10(this$static){
  checkCriticalState(this$static.last != -1);
  this$static.this$01_0.remove(this$static.last);
  this$static.i = this$static.last;
  this$static.last = -1;
}

function AbstractList$IteratorImpl(this$0){
  this.this$01_0 = this$0;
}

defineClass(106, 1, {}, AbstractList$IteratorImpl);
_.hasNext_0 = function hasNext_2(){
  return $hasNext(this);
}
;
_.next_1 = function next_3(){
  return $next_2(this);
}
;
_.remove_3 = function remove_21(){
  $remove_10(this);
}
;
_.i = 0;
_.last = -1;
var Ljava_util_AbstractList$IteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/IteratorImpl', 106);
function AbstractList$ListIteratorImpl(this$0, start_0){
  this.this$01 = this$0;
  AbstractList$IteratorImpl.call(this, this$0);
  checkCriticalPositionIndex(start_0, this$0.size_1());
  this.i = start_0;
}

defineClass(100, 106, {}, AbstractList$ListIteratorImpl);
_.remove_3 = function remove_22(){
  $remove_10(this);
}
;
_.hasPrevious = function hasPrevious(){
  return this.i > 0;
}
;
_.previous = function previous_0(){
  return checkCriticalElement(this.i > 0) , this.this$01.get_0(this.last = --this.i);
}
;
var Ljava_util_AbstractList$ListIteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/ListIteratorImpl', 100);
function AbstractMap$1(this$0){
  this.this$01 = this$0;
}

defineClass(124, $intern_48, $intern_49, AbstractMap$1);
_.contains_0 = function contains_2(key){
  return $containsKey_0(this.this$01, key);
}
;
_.iterator = function iterator_7(){
  var outerIter;
  return outerIter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this.this$01)).this$01) , new AbstractMap$1$1(outerIter);
}
;
_.size_1 = function size_7(){
  return $size(this.this$01);
}
;
var Ljava_util_AbstractMap$1_2_classLit = createForClass('java.util', 'AbstractMap/1', 124);
function AbstractMap$1$1(val$outerIter){
  this.val$outerIter2 = val$outerIter;
}

defineClass(148, 1, {}, AbstractMap$1$1);
_.hasNext_0 = function hasNext_3(){
  return this.val$outerIter2.hasNext;
}
;
_.next_1 = function next_4(){
  var entry;
  return entry = $next_1(this.val$outerIter2) , entry.getKey();
}
;
_.remove_3 = function remove_25(){
  $remove_9(this.val$outerIter2);
}
;
var Ljava_util_AbstractMap$1$1_2_classLit = createForClass('java.util', 'AbstractMap/1/1', 148);
function $setValue_0(this$static, value_0){
  var oldValue;
  oldValue = this$static.value_0;
  this$static.value_0 = value_0;
  return oldValue;
}

defineClass(193, 1, {193:1, 44:1});
_.equals_0 = function equals_29(other){
  var entry;
  if (!instanceOf(other, 44)) {
    return false;
  }
  entry = castTo(other, 44);
  return equals_38(this.key_0, entry.getKey()) && equals_38(this.value_0, entry.getValue_0());
}
;
_.getKey = function getKey(){
  return this.key_0;
}
;
_.getValue_0 = function getValue_2(){
  return this.value_0;
}
;
_.hashCode_0 = function hashCode_26(){
  return hashCode_36(this.key_0) ^ hashCode_36(this.value_0);
}
;
_.setValue = function setValue(value_0){
  return $setValue_0(this, value_0);
}
;
_.toString_0 = function toString_52(){
  return this.key_0 + '=' + this.value_0;
}
;
var Ljava_util_AbstractMap$AbstractEntry_2_classLit = createForClass('java.util', 'AbstractMap/AbstractEntry', 193);
function AbstractMap$SimpleEntry(key, value_0){
  this.key_0 = key;
  this.value_0 = value_0;
}

defineClass(147, 193, {193:1, 147:1, 44:1}, AbstractMap$SimpleEntry);
var Ljava_util_AbstractMap$SimpleEntry_2_classLit = createForClass('java.util', 'AbstractMap/SimpleEntry', 147);
defineClass(1025, 1, {44:1});
_.equals_0 = function equals_30(other){
  var entry;
  if (!instanceOf(other, 44)) {
    return false;
  }
  entry = castTo(other, 44);
  return equals_38(this.val$entry2.value[0], entry.getKey()) && equals_38($getValue_1(this), entry.getValue_0());
}
;
_.hashCode_0 = function hashCode_27(){
  return hashCode_36(this.val$entry2.value[0]) ^ hashCode_36($getValue_1(this));
}
;
_.toString_0 = function toString_53(){
  return this.val$entry2.value[0] + '=' + $getValue_1(this);
}
;
var Ljava_util_AbstractMapEntry_2_classLit = createForClass('java.util', 'AbstractMapEntry', 1025);
function $$init_2(this$static){
  this$static.array = initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_13, 1, 0, 5, 1);
}

function $add_12(this$static, index_0, o){
  checkCriticalPositionIndex(index_0, this$static.array.length);
  insertTo(this$static.array, index_0, o);
}

function $add_13(this$static, o){
  this$static.array[this$static.array.length] = o;
  return true;
}

function $addAll_1(this$static, c){
  var cArray, len;
  cArray = c.toArray();
  len = cArray.length;
  if (len == 0) {
    return false;
  }
  insertTo_0(this$static.array, this$static.array.length, cArray);
  return true;
}

function $get_5(this$static, index_0){
  checkCriticalElementIndex(index_0, this$static.array.length);
  return this$static.array[index_0];
}

function $indexOf_2(this$static, o, index_0){
  for (; index_0 < this$static.array.length; ++index_0) {
    if (equals_38(o, this$static.array[index_0])) {
      return index_0;
    }
  }
  return -1;
}

function $remove_11(this$static, index_0){
  var previous;
  previous = (checkCriticalElementIndex(index_0, this$static.array.length) , this$static.array[index_0]);
  removeFrom(this$static.array, index_0);
  return previous;
}

function $toArray_0(this$static){
  return clone_0(this$static.array, this$static.array.length);
}

function $toArray_1(this$static, out){
  var i, size_0;
  size_0 = this$static.array.length;
  out.length < size_0 && (out = stampJavaTypeInfo_1(new Array(size_0), out));
  for (i = 0; i < size_0; ++i) {
    setCheck(out, i, this$static.array[i]);
  }
  out.length > size_0 && setCheck(out, size_0, null);
  return out;
}

function ArrayList(){
  $$init_2(this);
}

function ArrayList_1(c){
  $$init_2(this);
  insertTo_0(this.array, 0, c.toArray());
}

defineClass(11, $intern_50, $intern_52, ArrayList, ArrayList_1);
_.add_1 = function add_11(index_0, o){
  $add_12(this, index_0, o);
}
;
_.add_0 = function add_12(o){
  return $add_13(this, o);
}
;
_.contains_0 = function contains_6(o){
  return $indexOf_2(this, o, 0) != -1;
}
;
_.get_0 = function get_10(index_0){
  return $get_5(this, index_0);
}
;
_.isEmpty = function isEmpty_2(){
  return this.array.length == 0;
}
;
_.iterator = function iterator_12(){
  return new ArrayList$1(this);
}
;
_.remove = function remove_31(index_0){
  return $remove_11(this, index_0);
}
;
_.size_1 = function size_11(){
  return this.array.length;
}
;
_.toArray = function toArray_3(){
  return $toArray_0(this);
}
;
_.toArray_0 = function toArray_4(out){
  return $toArray_1(this, out);
}
;
var Ljava_util_ArrayList_2_classLit = createForClass('java.util', 'ArrayList', 11);
function $next_3(this$static){
  checkCriticalElement(this$static.i < this$static.this$01.array.length);
  this$static.last = this$static.i++;
  return this$static.this$01.array[this$static.last];
}

function $remove_13(this$static){
  checkCriticalState(this$static.last != -1);
  $remove_11(this$static.this$01, this$static.i = this$static.last);
  this$static.last = -1;
}

function ArrayList$1(this$0){
  this.this$01 = this$0;
}

defineClass(13, 1, {}, ArrayList$1);
_.hasNext_0 = function hasNext_6(){
  return this.i < this.this$01.array.length;
}
;
_.next_1 = function next_7(){
  return $next_3(this);
}
;
_.remove_3 = function remove_33(){
  $remove_13(this);
}
;
_.i = 0;
_.last = -1;
var Ljava_util_ArrayList$1_2_classLit = createForClass('java.util', 'ArrayList/1', 13);
function hashCode_28(a){
  var e, e$index, e$max, hashCode;
  if (a == null) {
    return 0;
  }
  hashCode = 1;
  for (e$index = 0 , e$max = a.length; e$index < e$max; ++e$index) {
    e = a[e$index];
    hashCode = 31 * hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = hashCode | 0;
  }
  return hashCode;
}

function $clinit_Collections(){
  $clinit_Collections = emptyMethod;
  EMPTY_LIST = new Collections$EmptyList;
  EMPTY_MAP_0 = new Collections$EmptyMap;
  EMPTY_SET = new Collections$EmptySet;
}

function hashCode_29(collection){
  $clinit_Collections();
  var e, e$iterator, hashCode;
  hashCode = 0;
  for (e$iterator = collection.iterator(); e$iterator.hasNext_0();) {
    e = e$iterator.next_1();
    hashCode = hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = hashCode | 0;
  }
  return hashCode;
}

function hashCode_30(list){
  $clinit_Collections();
  var e, e$iterator, hashCode;
  hashCode = 1;
  for (e$iterator = list.iterator(); e$iterator.hasNext_0();) {
    e = e$iterator.next_1();
    hashCode = 31 * hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = hashCode | 0;
  }
  return hashCode;
}

function unmodifiableList(list){
  $clinit_Collections();
  return instanceOf(list, 214)?new Collections$UnmodifiableRandomAccessList(list):new Collections$UnmodifiableList(list);
}

var EMPTY_LIST, EMPTY_MAP_0, EMPTY_SET;
function Collections$EmptyList(){
}

defineClass(538, $intern_50, $intern_52, Collections$EmptyList);
_.contains_0 = function contains_8(object){
  return false;
}
;
_.get_0 = function get_12(location_0){
  checkCriticalElementIndex(location_0, 0);
  return null;
}
;
_.iterator = function iterator_13(){
  return $clinit_Collections() , $clinit_Collections$EmptyListIterator() , INSTANCE_32;
}
;
_.listIterator = function listIterator_3(){
  return $clinit_Collections() , $clinit_Collections$EmptyListIterator() , INSTANCE_32;
}
;
_.size_1 = function size_13(){
  return 0;
}
;
var Ljava_util_Collections$EmptyList_2_classLit = createForClass('java.util', 'Collections/EmptyList', 538);
function $clinit_Collections$EmptyListIterator(){
  $clinit_Collections$EmptyListIterator = emptyMethod;
  INSTANCE_32 = new Collections$EmptyListIterator;
}

function Collections$EmptyListIterator(){
}

defineClass(539, 1, {}, Collections$EmptyListIterator);
_.hasNext_0 = function hasNext_7(){
  return false;
}
;
_.hasPrevious = function hasPrevious_0(){
  return false;
}
;
_.next_1 = function next_8(){
  throw toJs(new NoSuchElementException);
}
;
_.previous = function previous_1(){
  throw toJs(new NoSuchElementException);
}
;
_.remove_3 = function remove_34(){
  throw toJs(new IllegalStateException);
}
;
var INSTANCE_32;
var Ljava_util_Collections$EmptyListIterator_2_classLit = createForClass('java.util', 'Collections/EmptyListIterator', 539);
function Collections$EmptyMap(){
}

defineClass(541, 1018, $intern_11, Collections$EmptyMap);
_.containsKey = function containsKey_3(key){
  return false;
}
;
_.entrySet_0 = function entrySet_2(){
  return $clinit_Collections() , EMPTY_SET;
}
;
_.get_1 = function get_13(key){
  return null;
}
;
_.size_1 = function size_14(){
  return 0;
}
;
var Ljava_util_Collections$EmptyMap_2_classLit = createForClass('java.util', 'Collections/EmptyMap', 541);
function Collections$EmptySet(){
}

defineClass(540, $intern_48, $intern_53, Collections$EmptySet);
_.contains_0 = function contains_9(object){
  return false;
}
;
_.iterator = function iterator_14(){
  return $clinit_Collections() , $clinit_Collections$EmptyListIterator() , INSTANCE_32;
}
;
_.size_1 = function size_15(){
  return 0;
}
;
var Ljava_util_Collections$EmptySet_2_classLit = createForClass('java.util', 'Collections/EmptySet', 540);
defineClass(318, 1, $intern_47);
_.add_0 = function add_14(o){
  throw toJs(new UnsupportedOperationException);
}
;
_.isEmpty = function isEmpty_3(){
  return this.coll.isEmpty();
}
;
_.iterator = function iterator_15(){
  return new Collections$UnmodifiableCollectionIterator(this.coll.iterator());
}
;
_.size_1 = function size_16(){
  return this.coll.size_1();
}
;
_.toArray = function toArray_7(){
  return this.coll.toArray();
}
;
_.toString_0 = function toString_55(){
  return toString_36(this.coll);
}
;
var Ljava_util_Collections$UnmodifiableCollection_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollection', 318);
function $remove_14(){
  throw toJs(new UnsupportedOperationException);
}

function Collections$UnmodifiableCollectionIterator(it){
  this.it = it;
}

defineClass(133, 1, {}, Collections$UnmodifiableCollectionIterator);
_.hasNext_0 = function hasNext_8(){
  return this.it.hasNext_0();
}
;
_.next_1 = function next_9(){
  return this.it.next_1();
}
;
_.remove_3 = function remove_36(){
  $remove_14();
}
;
var Ljava_util_Collections$UnmodifiableCollectionIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollectionIterator', 133);
function Collections$UnmodifiableList(list){
  this.coll = list;
  this.list = list;
}

defineClass(255, 318, $intern_51, Collections$UnmodifiableList);
_.equals_0 = function equals_32(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.list, o);
}
;
_.get_0 = function get_14(index_0){
  return this.list.get_0(index_0);
}
;
_.hashCode_0 = function hashCode_31(){
  return hashCode__I__devirtual$(this.list);
}
;
_.isEmpty = function isEmpty_4(){
  return this.list.isEmpty();
}
;
_.listIterator = function listIterator_4(){
  return new Collections$UnmodifiableListIterator(this.list.listIterator_0(0));
}
;
_.listIterator_0 = function listIterator_5(from){
  return new Collections$UnmodifiableListIterator(this.list.listIterator_0(from));
}
;
var Ljava_util_Collections$UnmodifiableList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableList', 255);
function Collections$UnmodifiableListIterator(lit){
  Collections$UnmodifiableCollectionIterator.call(this, lit);
  this.lit = lit;
}

defineClass(320, 133, {}, Collections$UnmodifiableListIterator);
_.remove_3 = function remove_38(){
  $remove_14();
}
;
_.hasPrevious = function hasPrevious_1(){
  return this.lit.hasPrevious();
}
;
_.previous = function previous_2(){
  return this.lit.previous();
}
;
var Ljava_util_Collections$UnmodifiableListIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableListIterator', 320);
function $entrySet(this$static){
  !this$static.entrySet && (this$static.entrySet = new Collections$UnmodifiableMap$UnmodifiableEntrySet(this$static.map_0.entrySet_0()));
  return this$static.entrySet;
}

function Collections$UnmodifiableMap(map_0){
  this.map_0 = map_0;
}

defineClass(300, 1, $intern_10, Collections$UnmodifiableMap);
_.entrySet_0 = function entrySet_3(){
  return $entrySet(this);
}
;
_.equals_0 = function equals_33(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.map_0, o);
}
;
_.get_1 = function get_15(key){
  return this.map_0.get_1(key);
}
;
_.hashCode_0 = function hashCode_32(){
  return hashCode__I__devirtual$(this.map_0);
}
;
_.put = function put_2(key, value_0){
  throw toJs(new UnsupportedOperationException);
}
;
_.size_1 = function size_17(){
  return this.map_0.size_1();
}
;
_.toString_0 = function toString_56(){
  return toString_36(this.map_0);
}
;
var Ljava_util_Collections$UnmodifiableMap_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap', 300);
defineClass(542, 318, $intern_49);
_.equals_0 = function equals_34(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.coll, o);
}
;
_.hashCode_0 = function hashCode_33(){
  return hashCode__I__devirtual$(this.coll);
}
;
var Ljava_util_Collections$UnmodifiableSet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableSet', 542);
function $wrap(array, size_0){
  var i;
  for (i = 0; i < size_0; ++i) {
    setCheck(array, i, new Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(castTo(array[i], 44)));
  }
}

function Collections$UnmodifiableMap$UnmodifiableEntrySet(s){
  this.coll = s;
}

defineClass(543, 542, $intern_49, Collections$UnmodifiableMap$UnmodifiableEntrySet);
_.iterator = function iterator_16(){
  var it;
  return it = this.coll.iterator() , new Collections$UnmodifiableMap$UnmodifiableEntrySet$1(it);
}
;
_.toArray = function toArray_9(){
  var array;
  array = this.coll.toArray();
  $wrap(array, array.length);
  return array;
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet', 543);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$1(val$it){
  this.val$it2 = val$it;
}

defineClass(321, 1, {}, Collections$UnmodifiableMap$UnmodifiableEntrySet$1);
_.next_1 = function next_10(){
  return new Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(castTo(this.val$it2.next_1(), 44));
}
;
_.hasNext_0 = function hasNext_9(){
  return this.val$it2.hasNext_0();
}
;
_.remove_3 = function remove_40(){
  throw toJs(new UnsupportedOperationException);
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$1_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/1', 321);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(entry){
  this.entry = entry;
}

defineClass(256, 1, {44:1}, Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry);
_.equals_0 = function equals_35(o){
  return this.entry.equals_0(o);
}
;
_.getKey = function getKey_0(){
  return this.entry.getKey();
}
;
_.getValue_0 = function getValue_3(){
  return this.entry.getValue_0();
}
;
_.hashCode_0 = function hashCode_34(){
  return this.entry.hashCode_0();
}
;
_.setValue = function setValue_0(value_0){
  throw toJs(new UnsupportedOperationException);
}
;
_.toString_0 = function toString_57(){
  return toString_36(this.entry);
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/UnmodifiableEntry', 256);
function Collections$UnmodifiableRandomAccessList(list){
  Collections$UnmodifiableList.call(this, list);
}

defineClass(319, 255, {21:1, 49:1, 40:1, 214:1}, Collections$UnmodifiableRandomAccessList);
var Ljava_util_Collections$UnmodifiableRandomAccessList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableRandomAccessList', 319);
function checkStructuralChange(host, iterator){
  if (iterator.$modCount != host.$modCount) {
    throw toJs(new ConcurrentModificationException);
  }
}

function recordLastKnownStructure(host, iterator){
  iterator.$modCount = host.$modCount;
}

function structureChanged(host){
  var modCount, modCountable;
  modCountable = host;
  modCount = modCountable.$modCount | 0;
  modCountable.$modCount = modCount + 1;
}

function ConcurrentModificationException(){
  RuntimeException.call(this);
}

defineClass(757, 10, $intern_5, ConcurrentModificationException);
var Ljava_util_ConcurrentModificationException_2_classLit = createForClass('java.util', 'ConcurrentModificationException', 757);
function $toString_6(this$static){
  var hourOffset, minuteOffset, offset;
  offset = -this$static.jsdate.getTimezoneOffset();
  hourOffset = (offset >= 0?'+':'') + (offset / 60 | 0);
  minuteOffset = padTwo($wnd.Math.abs(offset) % 60);
  return ($clinit_Date$StringData() , DAYS)[this$static.jsdate.getDay()] + ' ' + MONTHS[this$static.jsdate.getMonth()] + ' ' + padTwo(this$static.jsdate.getDate()) + ' ' + padTwo(this$static.jsdate.getHours()) + ':' + padTwo(this$static.jsdate.getMinutes()) + ':' + padTwo(this$static.jsdate.getSeconds()) + ' GMT' + hourOffset + minuteOffset + ' ' + this$static.jsdate.getFullYear();
}

function Date_1(date){
  this.jsdate = new $wnd.Date(toDouble_0(date));
}

function padTwo(number){
  return number < 10?'0' + number:'' + number;
}

defineClass(138, 1, $intern_54, Date_1);
_.equals_0 = function equals_37(obj){
  return instanceOf(obj, 138) && eq(fromDouble_0(this.jsdate.getTime()), fromDouble_0(castTo(obj, 138).jsdate.getTime()));
}
;
_.hashCode_0 = function hashCode_35(){
  var time;
  time = fromDouble_0(this.jsdate.getTime());
  return toInt(xor_0(time, shru_0(time, 32)));
}
;
_.toString_0 = function toString_58(){
  return $toString_6(this);
}
;
var Ljava_util_Date_2_classLit = createForClass('java.util', 'Date', 138);
function $clinit_Date$StringData(){
  $clinit_Date$StringData = emptyMethod;
  DAYS = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']);
  MONTHS = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']);
}

var DAYS, MONTHS;
function $add_14(this$static, o){
  var old;
  old = $put(this$static.map_0, o, this$static);
  return old == null;
}

function $contains_4(this$static, o){
  return $containsKey_0(this$static.map_0, o);
}

function HashSet(){
  this.map_0 = new HashMap;
}

defineClass(112, $intern_48, $intern_53, HashSet);
_.add_0 = function add_16(o){
  return $add_14(this, o);
}
;
_.contains_0 = function contains_12(o){
  return $contains_4(this, o);
}
;
_.isEmpty = function isEmpty_6(){
  return $size(this.map_0) == 0;
}
;
_.iterator = function iterator_17(){
  var outerIter;
  return outerIter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet((new AbstractMap$1(this.map_0)).this$01)).this$01) , new AbstractMap$1$1(outerIter);
}
;
_.size_1 = function size_18(){
  return $size(this.map_0);
}
;
var Ljava_util_HashSet_2_classLit = createForClass('java.util', 'HashSet', 112);
function $findEntryInChain(this$static, key, chain){
  var entry, entry$index, entry$max;
  for (entry$index = 0 , entry$max = chain.length; entry$index < entry$max; ++entry$index) {
    entry = chain[entry$index];
    if (this$static.host.equals_1(key, entry.getKey())) {
      return entry;
    }
  }
  return null;
}

function $getChainOrEmpty(this$static, hashCode){
  var chain;
  chain = this$static.backingMap.get(hashCode);
  return chain == null?new Array:chain;
}

function $getEntry(this$static, key){
  return $findEntryInChain(this$static, key, $getChainOrEmpty(this$static, key == null?0:this$static.host.getHashCode(key)));
}

function $put_1(this$static, key, value_0){
  var chain, chain0, entry, hashCode;
  hashCode = key == null?0:this$static.host.getHashCode(key);
  chain0 = (chain = this$static.backingMap.get(hashCode) , chain == null?new Array:chain);
  if (chain0.length == 0) {
    this$static.backingMap.set(hashCode, chain0);
  }
   else {
    entry = $findEntryInChain(this$static, key, chain0);
    if (entry) {
      return entry.setValue(value_0);
    }
  }
  setCheck(chain0, chain0.length, new AbstractMap$SimpleEntry(key, value_0));
  ++this$static.size_0;
  structureChanged(this$static.host);
  return null;
}

function $remove_16(this$static, key){
  var chain, chain0, entry, hashCode, i;
  hashCode = key == null?0:this$static.host.getHashCode(key);
  chain0 = (chain = this$static.backingMap.get(hashCode) , chain == null?new Array:chain);
  for (i = 0; i < chain0.length; i++) {
    entry = chain0[i];
    if (this$static.host.equals_1(key, entry.getKey())) {
      if (chain0.length == 1) {
        chain0.length = 0;
        $delete(this$static.backingMap, hashCode);
      }
       else {
        chain0.splice(i, 1);
      }
      --this$static.size_0;
      structureChanged(this$static.host);
      return entry.getValue_0();
    }
  }
  return null;
}

function InternalHashCodeMap(host){
  this.backingMap = newJsMap();
  this.host = host;
}

defineClass(608, 1, $intern_42, InternalHashCodeMap);
_.iterator = function iterator_18(){
  return new InternalHashCodeMap$1(this);
}
;
_.size_0 = 0;
var Ljava_util_InternalHashCodeMap_2_classLit = createForClass('java.util', 'InternalHashCodeMap', 608);
function InternalHashCodeMap$1(this$0){
  this.this$01 = this$0;
  this.chains = this.this$01.backingMap.entries();
  this.chain = new Array;
}

defineClass(333, 1, {}, InternalHashCodeMap$1);
_.next_1 = function next_11(){
  return this.lastEntry = this.chain[this.itemIndex++] , this.lastEntry;
}
;
_.hasNext_0 = function hasNext_10(){
  var current;
  if (this.itemIndex < this.chain.length) {
    return true;
  }
  current = this.chains.next();
  if (!current.done) {
    this.chain = current.value[1];
    this.itemIndex = 0;
    return true;
  }
  return false;
}
;
_.remove_3 = function remove_42(){
  $remove_16(this.this$01, this.lastEntry.getKey());
  this.itemIndex != 0 && --this.itemIndex;
}
;
_.itemIndex = 0;
_.lastEntry = null;
var Ljava_util_InternalHashCodeMap$1_2_classLit = createForClass('java.util', 'InternalHashCodeMap/1', 333);
function $delete(this$static, key){
  var fn;
  fn = this$static['delete'];
  fn.call(this$static, key);
}

function $delete_0(this$static, key){
  var fn;
  fn = this$static['delete'];
  fn.call(this$static, key);
}

function $clinit_InternalJsMapFactory(){
  $clinit_InternalJsMapFactory = emptyMethod;
  jsMapCtor = getJsMapConstructor();
}

function canHandleObjectCreateAndProto(){
  if (!Object.create || !Object.getOwnPropertyNames) {
    return false;
  }
  var protoField = '__proto__';
  var map_0 = Object.create(null);
  if (map_0[protoField] !== undefined) {
    return false;
  }
  var keys_0 = Object.getOwnPropertyNames(map_0);
  if (keys_0.length != 0) {
    return false;
  }
  map_0[protoField] = 42;
  if (map_0[protoField] !== 42) {
    return false;
  }
  if (Object.getOwnPropertyNames(map_0).length == 0) {
    return false;
  }
  return true;
}

function getJsMapConstructor(){
  function isCorrectIterationProtocol(){
    try {
      return (new Map).entries().next().done;
    }
     catch (e) {
      return false;
    }
  }

  if (typeof Map === 'function' && Map.prototype.entries && isCorrectIterationProtocol()) {
    return Map;
  }
   else {
    return getJsMapPolyFill();
  }
}

function getJsMapPolyFill(){
  function Stringmap(){
    this.obj = this.createObject();
  }

  ;
  Stringmap.prototype.createObject = function(key){
    return Object.create(null);
  }
  ;
  Stringmap.prototype.get = function(key){
    return this.obj[key];
  }
  ;
  Stringmap.prototype.set = function(key, value_0){
    this.obj[key] = value_0;
  }
  ;
  Stringmap.prototype['delete'] = function(key){
    delete this.obj[key];
  }
  ;
  Stringmap.prototype.keys = function(){
    return Object.getOwnPropertyNames(this.obj);
  }
  ;
  Stringmap.prototype.entries = function(){
    var keys_0 = this.keys();
    var map_0 = this;
    var nextIndex = 0;
    return {next:function(){
      if (nextIndex >= keys_0.length)
        return {done:true};
      var key = keys_0[nextIndex++];
      return {value:[key, map_0.get(key)], done:false};
    }
    };
  }
  ;
  if (!canHandleObjectCreateAndProto()) {
    Stringmap.prototype.createObject = function(){
      return {};
    }
    ;
    Stringmap.prototype.get = function(key){
      return this.obj[':' + key];
    }
    ;
    Stringmap.prototype.set = function(key, value_0){
      this.obj[':' + key] = value_0;
    }
    ;
    Stringmap.prototype['delete'] = function(key){
      delete this.obj[':' + key];
    }
    ;
    Stringmap.prototype.keys = function(){
      var result = [];
      for (var key in this.obj) {
        key.charCodeAt(0) == 58 && result.push(key.substring(1));
      }
      return result;
    }
    ;
  }
  return Stringmap;
}

function newJsMap(){
  $clinit_InternalJsMapFactory();
  return new jsMapCtor;
}

var jsMapCtor;
function $contains_5(this$static, key){
  return !(this$static.backingMap.get(key) === undefined);
}

function $get_7(this$static, key){
  return this$static.backingMap.get(key);
}

function $put_2(this$static, key, value_0){
  var oldValue;
  oldValue = this$static.backingMap.get(key);
  this$static.backingMap.set(key, value_0 === undefined?null:value_0);
  if (oldValue === undefined) {
    ++this$static.size_0;
    structureChanged(this$static.host);
  }
   else {
    ++this$static.valueMod;
  }
  return oldValue;
}

function $remove_17(this$static, key){
  var value_0;
  value_0 = this$static.backingMap.get(key);
  if (value_0 === undefined) {
    ++this$static.valueMod;
  }
   else {
    $delete_0(this$static.backingMap, key);
    --this$static.size_0;
    structureChanged(this$static.host);
  }
  return value_0;
}

function InternalStringMap(host){
  this.backingMap = newJsMap();
  this.host = host;
}

defineClass(606, 1, $intern_42, InternalStringMap);
_.iterator = function iterator_19(){
  return new InternalStringMap$1(this);
}
;
_.size_0 = 0;
_.valueMod = 0;
var Ljava_util_InternalStringMap_2_classLit = createForClass('java.util', 'InternalStringMap', 606);
function InternalStringMap$1(this$0){
  this.this$01 = this$0;
  this.entries_0 = this.this$01.backingMap.entries();
  this.current = this.entries_0.next();
}

defineClass(332, 1, {}, InternalStringMap$1);
_.next_1 = function next_12(){
  return this.last = this.current , this.current = this.entries_0.next() , new InternalStringMap$2(this.this$01, this.last, this.this$01.valueMod);
}
;
_.hasNext_0 = function hasNext_11(){
  return !this.current.done;
}
;
_.remove_3 = function remove_43(){
  $remove_17(this.this$01, this.last.value[0]);
}
;
var Ljava_util_InternalStringMap$1_2_classLit = createForClass('java.util', 'InternalStringMap/1', 332);
function $getValue_1(this$static){
  if (this$static.this$01.valueMod != this$static.val$lastValueMod3) {
    return $get_7(this$static.this$01, this$static.val$entry2.value[0]);
  }
  return this$static.val$entry2.value[1];
}

function InternalStringMap$2(this$0, val$entry, val$lastValueMod){
  this.this$01 = this$0;
  this.val$entry2 = val$entry;
  this.val$lastValueMod3 = val$lastValueMod;
}

defineClass(607, 1025, {44:1}, InternalStringMap$2);
_.getKey = function getKey_1(){
  return this.val$entry2.value[0];
}
;
_.getValue_0 = function getValue_4(){
  return $getValue_1(this);
}
;
_.setValue = function setValue_1(object){
  return $put_2(this.this$01, this.val$entry2.value[0], object);
}
;
_.val$lastValueMod3 = 0;
var Ljava_util_InternalStringMap$2_2_classLit = createForClass('java.util', 'InternalStringMap/2', 607);
function $clinit_Locale(){
  $clinit_Locale = emptyMethod;
  ROOT = new Locale$1;
  ENGLISH = new Locale$2;
  defaultLocale = new Locale$4;
}

defineClass(1010, 1, {});
var ENGLISH, ROOT, defaultLocale;
var Ljava_util_Locale_2_classLit = createForClass('java.util', 'Locale', 1010);
function Locale$1(){
}

defineClass(412, 1010, {}, Locale$1);
_.toString_0 = function toString_59(){
  return '';
}
;
var Ljava_util_Locale$1_2_classLit = createForClass('java.util', 'Locale/1', 412);
function Locale$2(){
}

defineClass(413, 1010, {}, Locale$2);
_.toString_0 = function toString_60(){
  return 'en';
}
;
var Ljava_util_Locale$2_2_classLit = createForClass('java.util', 'Locale/2', 413);
function Locale$4(){
}

defineClass(414, 1010, {}, Locale$4);
_.toString_0 = function toString_61(){
  return 'unknown';
}
;
var Ljava_util_Locale$4_2_classLit = createForClass('java.util', 'Locale/4', 414);
function NoSuchElementException(){
  RuntimeException.call(this);
}

defineClass(108, 10, {3:1, 14:1, 10:1, 17:1, 108:1}, NoSuchElementException);
var Ljava_util_NoSuchElementException_2_classLit = createForClass('java.util', 'NoSuchElementException', 108);
function equals_38(a, b){
  return maskUndefined(a) === maskUndefined(b) || a != null && equals_Ljava_lang_Object__Z__devirtual$(a, b);
}

function hashCode_36(o){
  return o != null?hashCode__I__devirtual$(o):0;
}

function $add_17(this$static, newElement){
  !this$static.builder?(this$static.builder = new StringBuilder_1(this$static.prefix)):$append_5(this$static.builder, this$static.delimiter);
  $append_3(this$static.builder, newElement);
  return this$static;
}

function StringJoiner(prefix, suffix){
  this.delimiter = ', ';
  this.prefix = prefix;
  this.suffix = suffix;
  this.emptyValue = this.prefix + ('' + this.suffix);
}

defineClass(307, 1, {}, StringJoiner);
_.toString_0 = function toString_63(){
  return !this.builder?this.emptyValue:this.suffix.length == 0?this.builder.string:this.builder.string + ('' + this.suffix);
}
;
var Ljava_util_StringJoiner_2_classLit = createForClass('java.util', 'StringJoiner', 307);
function $clinit_Level(){
  $clinit_Level = emptyMethod;
  ALL = new Level$LevelAll;
  CONFIG = new Level$LevelConfig;
  FINE = new Level$LevelFine;
  FINER = new Level$LevelFiner;
  FINEST = new Level$LevelFinest;
  INFO = new Level$LevelInfo;
  OFF = new Level$LevelOff;
  SEVERE = new Level$LevelSevere;
  WARNING = new Level$LevelWarning;
}

function parse_0(name_0){
  $clinit_Level();
  var value_0;
  value_0 = $toUpperCase(name_0, ($clinit_Locale() , ROOT));
  switch (value_0) {
    case 'ALL':
      return ALL;
    case 'CONFIG':
      return CONFIG;
    case 'FINE':
      return FINE;
    case 'FINER':
      return FINER;
    case 'FINEST':
      return FINEST;
    case 'INFO':
      return INFO;
    case 'OFF':
      return OFF;
    case 'SEVERE':
      return SEVERE;
    case 'WARNING':
      return WARNING;
    default:throw toJs(new IllegalArgumentException_0('Invalid level "' + name_0 + '"'));
  }
}

defineClass($intern_55, 1, $intern_14);
_.getName = function getName_17(){
  return 'DUMMY';
}
;
_.intValue = function intValue(){
  return -1;
}
;
_.toString_0 = function toString_65(){
  return this.getName();
}
;
var ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING;
var Ljava_util_logging_Level_2_classLit = createForClass('java.util.logging', 'Level', $intern_55);
function Level$LevelAll(){
}

defineClass(496, $intern_55, $intern_14, Level$LevelAll);
_.getName = function getName_18(){
  return 'ALL';
}
;
_.intValue = function intValue_0(){
  return $intern_25;
}
;
var Ljava_util_logging_Level$LevelAll_2_classLit = createForClass('java.util.logging', 'Level/LevelAll', 496);
function Level$LevelConfig(){
}

defineClass(497, $intern_55, $intern_14, Level$LevelConfig);
_.getName = function getName_19(){
  return 'CONFIG';
}
;
_.intValue = function intValue_1(){
  return 700;
}
;
var Ljava_util_logging_Level$LevelConfig_2_classLit = createForClass('java.util.logging', 'Level/LevelConfig', 497);
function Level$LevelFine(){
}

defineClass(498, $intern_55, $intern_14, Level$LevelFine);
_.getName = function getName_20(){
  return 'FINE';
}
;
_.intValue = function intValue_2(){
  return 500;
}
;
var Ljava_util_logging_Level$LevelFine_2_classLit = createForClass('java.util.logging', 'Level/LevelFine', 498);
function Level$LevelFiner(){
}

defineClass(499, $intern_55, $intern_14, Level$LevelFiner);
_.getName = function getName_21(){
  return 'FINER';
}
;
_.intValue = function intValue_3(){
  return 400;
}
;
var Ljava_util_logging_Level$LevelFiner_2_classLit = createForClass('java.util.logging', 'Level/LevelFiner', 499);
function Level$LevelFinest(){
}

defineClass(500, $intern_55, $intern_14, Level$LevelFinest);
_.getName = function getName_22(){
  return 'FINEST';
}
;
_.intValue = function intValue_4(){
  return 300;
}
;
var Ljava_util_logging_Level$LevelFinest_2_classLit = createForClass('java.util.logging', 'Level/LevelFinest', 500);
function Level$LevelInfo(){
}

defineClass(501, $intern_55, $intern_14, Level$LevelInfo);
_.getName = function getName_23(){
  return 'INFO';
}
;
_.intValue = function intValue_5(){
  return 800;
}
;
var Ljava_util_logging_Level$LevelInfo_2_classLit = createForClass('java.util.logging', 'Level/LevelInfo', 501);
function Level$LevelOff(){
}

defineClass(502, $intern_55, $intern_14, Level$LevelOff);
_.getName = function getName_24(){
  return 'OFF';
}
;
_.intValue = function intValue_6(){
  return $intern_0;
}
;
var Ljava_util_logging_Level$LevelOff_2_classLit = createForClass('java.util.logging', 'Level/LevelOff', 502);
function Level$LevelSevere(){
}

defineClass(503, $intern_55, $intern_14, Level$LevelSevere);
_.getName = function getName_25(){
  return 'SEVERE';
}
;
_.intValue = function intValue_7(){
  return $intern_15;
}
;
var Ljava_util_logging_Level$LevelSevere_2_classLit = createForClass('java.util.logging', 'Level/LevelSevere', 503);
function Level$LevelWarning(){
}

defineClass(504, $intern_55, $intern_14, Level$LevelWarning);
_.getName = function getName_26(){
  return 'WARNING';
}
;
_.intValue = function intValue_8(){
  return 900;
}
;
var Ljava_util_logging_Level$LevelWarning_2_classLit = createForClass('java.util.logging', 'Level/LevelWarning', 504);
function $addLoggerImpl(this$static, logger){
  $putStringValue(this$static.loggerMap, ($clinit_Logger() , LOGGING_OFF)?null:logger.name_0, logger);
}

function $ensureLogger(this$static, name_0){
  var logger, newLogger, name_1, parentName;
  logger = castTo($getStringValue(this$static.loggerMap, name_0), 190);
  if (!logger) {
    newLogger = new Logger(name_0);
    name_1 = ($clinit_Logger() , LOGGING_OFF)?null:newLogger.name_0;
    parentName = $substring_0(name_1, 0, $wnd.Math.max(0, $lastIndexOf(name_1, fromCodePoint(46))));
    $setParent_0(newLogger, $ensureLogger(this$static, parentName));
    $putStringValue(this$static.loggerMap, LOGGING_OFF?null:newLogger.name_0, newLogger);
    return newLogger;
  }
  return logger;
}

function LogManager(){
  this.loggerMap = new HashMap;
}

function getLogManager(){
  var rootLogger;
  if (!singleton_1) {
    singleton_1 = new LogManager;
    rootLogger = new Logger('');
    $setLevel_0(rootLogger, ($clinit_Level() , INFO));
    $addLoggerImpl(singleton_1, rootLogger);
  }
  return singleton_1;
}

defineClass(425, 1, {}, LogManager);
var singleton_1;
var Ljava_util_logging_LogManager_2_classLit = createForClass('java.util.logging', 'LogManager', 425);
function $setLoggerName(this$static, newName){
  this$static.loggerName = newName;
}

function LogRecord(level, msg){
  this.level = level;
  this.msg = msg;
  this.millis_0 = ($clinit_System() , fromDouble_0(Date.now()));
}

defineClass(657, 1, $intern_14, LogRecord);
_.loggerName = '';
_.millis_0 = 0;
_.thrown = null;
var Ljava_util_logging_LogRecord_2_classLit = createForClass('java.util.logging', 'LogRecord', 657);
function $clinit_Logger(){
  $clinit_Logger = emptyMethod;
  LOGGING_OFF = false;
  ALL_ENABLED = true;
  INFO_ENABLED = true;
  WARNING_ENABLED = true;
  SEVERE_ENABLED = true;
}

function $actuallyLog(this$static, record){
  var handler, handler$array, handler$array0, handler$index, handler$index0, handler$max, handler$max0, logger;
  for (handler$array0 = $getHandlers(this$static) , handler$index0 = 0 , handler$max0 = handler$array0.length; handler$index0 < handler$max0; ++handler$index0) {
    handler = handler$array0[handler$index0];
    handler.publish(record);
  }
  logger = !LOGGING_OFF && this$static.useParentHandlers?LOGGING_OFF?null:this$static.parent_0:null;
  while (logger) {
    for (handler$array = $getHandlers(logger) , handler$index = 0 , handler$max = handler$array.length; handler$index < handler$max; ++handler$index) {
      handler = handler$array[handler$index];
      handler.publish(record);
    }
    logger = !LOGGING_OFF && logger.useParentHandlers?LOGGING_OFF?null:logger.parent_0:null;
  }
}

function $addHandler_1(this$static, handler){
  if (LOGGING_OFF) {
    return;
  }
  $add_13(this$static.handlers, handler);
}

function $getEffectiveLevel(this$static){
  var effectiveLevel, logger;
  if (this$static.level) {
    return this$static.level;
  }
  logger = LOGGING_OFF?null:this$static.parent_0;
  while (logger) {
    effectiveLevel = LOGGING_OFF?null:logger.level;
    if (effectiveLevel) {
      return effectiveLevel;
    }
    logger = LOGGING_OFF?null:logger.parent_0;
  }
  return $clinit_Level() , INFO;
}

function $getHandlers(this$static){
  if (LOGGING_OFF) {
    return initUnidimensionalArray(Ljava_util_logging_Handler_2_classLit, $intern_56, 168, 0, 0, 1);
  }
  return castTo($toArray_1(this$static.handlers, initUnidimensionalArray(Ljava_util_logging_Handler_2_classLit, $intern_56, 168, this$static.handlers.array.length, 0, 1)), 374);
}

function $isLoggable(this$static, messageLevel){
  return ALL_ENABLED?messageLevel.intValue() >= $getEffectiveLevel(this$static).intValue():INFO_ENABLED?messageLevel.intValue() >= ($clinit_Level() , 800):WARNING_ENABLED?messageLevel.intValue() >= ($clinit_Level() , 900):SEVERE_ENABLED && messageLevel.intValue() >= ($clinit_Level() , $intern_15);
}

function $log(this$static, level, msg, thrown){
  var record;
  (ALL_ENABLED?level.intValue() >= $getEffectiveLevel(this$static).intValue():INFO_ENABLED?level.intValue() >= ($clinit_Level() , 800):WARNING_ENABLED?level.intValue() >= ($clinit_Level() , 900):SEVERE_ENABLED && level.intValue() >= ($clinit_Level() , $intern_15)) && (record = new LogRecord(level, msg) , record.thrown = thrown , $setLoggerName(record, LOGGING_OFF?null:this$static.name_0) , $actuallyLog(this$static, record) , undefined);
}

function $setLevel_0(this$static, newLevel){
  if (LOGGING_OFF) {
    return;
  }
  this$static.level = newLevel;
}

function $setParent_0(this$static, newParent){
  if (LOGGING_OFF) {
    return;
  }
  !!newParent && (this$static.parent_0 = newParent);
}

function $setUseParentHandlers(this$static){
  if (LOGGING_OFF) {
    return;
  }
  this$static.useParentHandlers = false;
}

function Logger(name_0){
  $clinit_Logger();
  if (LOGGING_OFF) {
    return;
  }
  this.name_0 = name_0;
  this.useParentHandlers = true;
  this.handlers = new ArrayList;
}

function getLogger(name_0){
  $clinit_Logger();
  if (LOGGING_OFF) {
    return new Logger(null);
  }
  return $ensureLogger(getLogManager(), name_0);
}

defineClass(190, 1, {190:1}, Logger);
_.useParentHandlers = false;
var ALL_ENABLED = false, INFO_ENABLED = false, LOGGING_OFF = false, SEVERE_ENABLED = false, WARNING_ENABLED = false;
var Ljava_util_logging_Logger_2_classLit = createForClass('java.util.logging', 'Logger', 190);
function clone_0(array, toIndex){
  var result;
  result = array.slice(0, toIndex);
  return stampJavaTypeInfo_0(result, array);
}

function copy_0(src_0, srcOfs, dest, destOfs, len, overwrite){
  var batchEnd, batchStart, destArray, end, spliceArgs;
  if (maskUndefined(src_0) === maskUndefined(dest)) {
    src_0 = src_0.slice(srcOfs, srcOfs + len);
    srcOfs = 0;
  }
  destArray = dest;
  for (batchStart = srcOfs , end = srcOfs + len; batchStart < end;) {
    batchEnd = $wnd.Math.min(batchStart + 10000, end);
    len = batchEnd - batchStart;
    spliceArgs = src_0.slice(batchStart, batchEnd);
    spliceArgs.splice(0, 0, destOfs, overwrite?len:0);
    Array.prototype.splice.apply(destArray, spliceArgs);
    batchStart = batchEnd;
    destOfs += len;
  }
}

function insertTo(array, index_0, value_0){
  array.splice(index_0, 0, value_0);
}

function insertTo_0(array, index_0, values){
  copy_0(values, 0, array, index_0, values.length, false);
}

function removeFrom(array, index_0){
  array.splice(index_0, 1);
}

defineClass(1096, 1, {});
function stampJavaTypeInfo_1(array, referenceType){
  return stampJavaTypeInfo_0(array, referenceType);
}

defineClass(1043, 1, {});
var Ljavaemul_internal_ConsoleLogger_2_classLit = createForClass('javaemul.internal', 'ConsoleLogger', 1043);
function checkCriticalArgument(expression, errorMessage){
  if (!expression) {
    throw toJs(new IllegalArgumentException_0(errorMessage));
  }
}

function checkCriticalArrayType(expression){
  if (!expression) {
    throw toJs(new ArrayStoreException);
  }
}

function checkCriticalElement(expression){
  if (!expression) {
    throw toJs(new NoSuchElementException);
  }
}

function checkCriticalElementIndex(index_0, size_0){
  if (index_0 < 0 || index_0 >= size_0) {
    throw toJs(new IndexOutOfBoundsException_0('Index: ' + index_0 + ', Size: ' + size_0));
  }
}

function checkCriticalNotNull(reference){
  if (reference == null) {
    throw toJs(new NullPointerException);
  }
  return reference;
}

function checkCriticalNotNull_0(reference, errorMessage){
  if (reference == null) {
    throw toJs(new NullPointerException_1(errorMessage));
  }
}

function checkCriticalPositionIndex(index_0, size_0){
  if (index_0 < 0 || index_0 > size_0) {
    throw toJs(new IndexOutOfBoundsException_0('Index: ' + index_0 + ', Size: ' + size_0));
  }
}

function checkCriticalState(expression){
  if (!expression) {
    throw toJs(new IllegalStateException);
  }
}

function checkCriticalStringElementIndex(index_0, size_0){
  if (index_0 < 0 || index_0 >= size_0) {
    throw toJs(new StringIndexOutOfBoundsException('Index: ' + index_0 + ', Size: ' + size_0));
  }
}

function checkCriticalType(expression){
  if (!expression) {
    throw toJs(new ClassCastException);
  }
}

function setPropertySafe(map_0, key, value_0){
  try {
    map_0[key] = value_0;
  }
   catch (ignored) {
  }
}

defineClass(1093, 1, {});
function getHashCode_0(o){
  return o.$H || (o.$H = ++nextHashId);
}

var nextHashId = 0;
function $clinit_StringHashCache(){
  $clinit_StringHashCache = emptyMethod;
  back_0 = new Object_0;
  front = new Object_0;
}

function compute(str){
  var hashCode, i, n, nBatch;
  hashCode = 0;
  n = str.length;
  nBatch = n - 4;
  i = 0;
  while (i < nBatch) {
    hashCode = (checkCriticalStringElementIndex(i + 3, str.length) , str.charCodeAt(i + 3) + (checkCriticalStringElementIndex(i + 2, str.length) , 31 * (str.charCodeAt(i + 2) + (checkCriticalStringElementIndex(i + 1, str.length) , 31 * (str.charCodeAt(i + 1) + (checkCriticalStringElementIndex(i, str.length) , 31 * (str.charCodeAt(i) + 31 * hashCode)))))));
    hashCode = hashCode | 0;
    i += 4;
  }
  while (i < n) {
    hashCode = hashCode * 31 + $charAt(str, i++);
  }
  hashCode = hashCode | 0;
  return hashCode;
}

function getHashCode_1(str){
  $clinit_StringHashCache();
  var hashCode, key, result;
  key = ':' + str;
  result = front[key];
  if (result != null) {
    return round_int((checkCriticalNotNull(result) , result));
  }
  result = back_0[key];
  hashCode = result == null?compute(str):round_int((checkCriticalNotNull(result) , result));
  increment();
  front[key] = hashCode;
  return hashCode;
}

function increment(){
  if (count_0 == 256) {
    back_0 = front;
    front = new Object_0;
    count_0 = 0;
  }
  ++count_0;
}

var back_0, count_0 = 0, front;
function getLogger_0(name_0){
  return $getLogger(($clinit_Impl_0() , LOGGER_FACTORY), name_0);
}

defineClass(1028, 1, {375:1});
var Lorg_slf4j_helpers_NamedLoggerBase_2_classLit = createForClass('org.slf4j.helpers', 'NamedLoggerBase', 1028);
defineClass(1029, 1028, {375:1});
var Lorg_slf4j_helpers_MarkerIgnoringBase_2_classLit = createForClass('org.slf4j.helpers', 'MarkerIgnoringBase', 1029);
function $info(this$static, msg){
  $log_0(this$static, ($clinit_Level() , INFO), msg, null);
}

function $log_0(this$static, level, msg, t){
  $isLoggable(this$static.logger, level) && $log(this$static.logger, level, msg, t);
}

function GWTLoggerAdapter(name_0){
  this.logger = getLogger(name_0);
}

defineClass(638, 1029, {375:1}, GWTLoggerAdapter);
var Lru_finam_slf4jgwt_logging_gwt_GWTLoggerAdapter_2_classLit = createForClass('ru.finam.slf4jgwt.logging.gwt', 'GWTLoggerAdapter', 638);
function $getLogger(this$static, name_0){
  var logger;
  if (name_0 == null) {
    throw toJs(new NullPointerException);
  }
  $equalsIgnoreCase('ROOT', name_0) && (name_0 = '');
  logger = castTo($getStringValue(this$static.loggers, name_0), 375);
  if (!logger) {
    logger = new GWTLoggerAdapter(name_0);
    $putStringValue(this$static.loggers, name_0, logger);
  }
  return logger;
}

function GWTLoggerFactory(){
  this.loggers = new HashMap;
}

defineClass(537, 1, {}, GWTLoggerFactory);
var Lru_finam_slf4jgwt_logging_gwt_GWTLoggerFactory_2_classLit = createForClass('ru.finam.slf4jgwt.logging.gwt', 'GWTLoggerFactory', 537);
function $clinit_Impl_0(){
  $clinit_Impl_0 = emptyMethod;
  LOGGER_FACTORY = new GWTLoggerFactory;
}

var LOGGER_FACTORY;
var I_classLit = createForPrimitive('int', 'I');
var Z_classLit = createForPrimitive('boolean', 'Z');
var $entry = ($clinit_Impl() , entry_0);
var gwtOnLoad = gwtOnLoad = gwtOnLoad_0;
addInitFunctions(init_0);
setGwtProperty('permProps', [[['locale', 'default'], ['user.agent', 'ie10']]]);
$sendStats('moduleStartup', 'moduleEvalEnd');
gwtOnLoad(__gwtModuleFunction.__errFn, __gwtModuleFunction.__moduleName, __gwtModuleFunction.__moduleBase, __gwtModuleFunction.__softPermutationId,__gwtModuleFunction.__computePropValue);
$sendStats('moduleStartup', 'end');
$gwt && $gwt.permProps && __gwtModuleFunction.__moduleStartupDone($gwt.permProps);
//# sourceURL=baseletgwt-0.js

