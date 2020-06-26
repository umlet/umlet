var $wnd = $wnd || window.parent;
var __gwtModuleFunction = $wnd.baseletgwt;
var $sendStats = __gwtModuleFunction.__sendStats;
$sendStats('moduleStartup', 'moduleEvalStart');
var $gwt_version = "2.8.2";
var $strongName = 'BF765B45A2AF2C32ECC87CF0779E0B3B';
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
var $intern_0 = 2147483647, $intern_1 = {3:1, 4:1}, $intern_2 = {3:1, 9:1, 8:1}, $intern_3 = {3:1, 6:1, 7:1, 4:1}, $intern_4 = {3:1, 6:1, 4:1}, $intern_5 = {3:1, 13:1, 10:1, 17:1}, $intern_6 = {3:1, 6:1, 7:1, 4:1, 34:1}, $intern_7 = -2147483648, $intern_8 = 1000, $intern_9 = {27:1, 23:1}, $intern_10 = {32:1, 22:1, 30:1, 27:1, 33:1, 23:1, 24:1}, $intern_11 = {32:1, 22:1, 30:1, 27:1, 52:1, 33:1, 23:1, 24:1, 21:1}, $intern_12 = {82:1}, $intern_13 = {3:1, 82:1}, $intern_14 = 3.141592653589793, $intern_15 = 6.283185307179586, $intern_16 = {50:1, 3:1}, $intern_17 = {3:1}, $intern_18 = {42:1, 35:1, 3:1, 9:1, 8:1}, $intern_19 = {35:1, 117:1, 3:1, 9:1, 8:1}, $intern_20 = {68:1, 3:1, 9:1, 8:1}, $intern_21 = {35:1, 182:1, 3:1, 9:1, 8:1}, $intern_22 = {22:1}, $intern_23 = {135:1, 136:1, 3:1, 13:1, 10:1, 17:1}, $intern_24 = 65535, $intern_25 = 4194303, $intern_26 = 1048575, $intern_27 = 524288, $intern_28 = 4194304, $intern_29 = 17592186044416, $intern_30 = -17592186044416, $intern_31 = {179:1}, $intern_32 = 4096, $intern_33 = 2048, $intern_34 = 32768, $intern_35 = 16384, $intern_36 = 65536, $intern_37 = 131072, $intern_38 = 262144, $intern_39 = 1048576, $intern_40 = 2097152, $intern_41 = 8388608, $intern_42 = 16777216, $intern_43 = 33554432, $intern_44 = 67108864, $intern_45 = {32:1, 22:1, 30:1, 27:1, 52:1, 33:1, 64:1, 23:1, 24:1, 21:1}, $intern_46 = {306:1, 26:1}, $intern_47 = {32:1, 22:1, 30:1, 27:1, 52:1, 33:1, 204:1, 23:1, 24:1, 21:1}, $intern_48 = {1002:1, 26:1}, $intern_49 = {21:1}, $intern_50 = {3:1, 153:1, 17:1}, $intern_51 = {3:1, 13:1, 67:1, 10:1, 17:1}, $intern_52 = {3:1, 9:1, 47:1, 231:1}, $intern_53 = {21:1, 51:1}, $intern_54 = 1039, $intern_55 = {21:1, 51:1, 107:1}, $intern_56 = 1035, $intern_57 = {21:1, 51:1, 41:1}, $intern_58 = {3:1, 21:1, 51:1, 41:1, 227:1}, $intern_59 = {3:1, 21:1, 51:1, 107:1}, $intern_60 = {3:1, 9:1, 98:1}, $intern_61 = 1037, $intern_62 = {3:1, 4:1, 386:1};
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

function $getCanonicalName(this$static){
  $ensureNamesAreInitialized(this$static);
  return this$static.canonicalName;
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

defineClass(316, 1, {}, Class_0);
_.createClassLiteralForArray = function createClassLiteralForArray(dimensions){
  var clazz;
  clazz = new Class_0;
  clazz.modifiers = 4;
  dimensions > 1?(clazz.componentType = getClassLiteralForArray_0(this, dimensions - 1)):(clazz.componentType = this);
  return clazz;
}
;
_.getCanonicalName = function getCanonicalName(){
  return $getCanonicalName(this);
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
var Ljava_lang_Class_2_classLit = createForClass('java.lang', 'Class', 316);
function $clinit_SharedConfig(){
  $clinit_SharedConfig = emptyMethod;
  instance_0 = new SharedConfig;
}

function $setDev_mode(this$static, dev_mode){
  this$static.dev_mode = dev_mode;
}

function SharedConfig(){
}

defineClass(435, 1, {}, SharedConfig);
_.dev_mode = false;
_.show_stickingpolygon = true;
_.stickingEnabled = true;
var instance_0;
var Lcom_baselet_control_config_SharedConfig_2_classLit = createForClass('com.baselet.control.config', 'SharedConfig', 435);
function $toString(this$static){
  return this$static.name_0 != null?this$static.name_0:'' + this$static.ordinal;
}

function Enum(name_0, ordinal){
  this.name_0 = name_0;
  this.ordinal = ordinal;
}

function createValueOfMap(enumConstants){
  var result, value_0, value$index, value$max;
  result = {};
  for (value$index = 0 , value$max = enumConstants.length; value$index < value$max; ++value$index) {
    value_0 = enumConstants[value$index];
    result[':' + (value_0.name_0 != null?value_0.name_0:'' + value_0.ordinal)] = value_0;
  }
  return result;
}

function valueOf(map_0, name_0){
  var result;
  checkCriticalNotNull(name_0);
  result = map_0[':' + name_0];
  checkCriticalArgument_0(!!result, 'Enum constant undefined: %s', stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_1, 1, 5, [name_0]));
  return result;
}

defineClass(8, 1, $intern_2);
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

defineClass(434, 1, {}, Program);
var instance_1, log_2;
var Lcom_baselet_control_enums_Program_2_classLit = createForClass('com.baselet.control.enums', 'Program', 434);
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
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_control_enums_RuntimeType_2_classLit, 1), $intern_3, 180, 0, [STANDALONE, ECLIPSE_PLUGIN, BATCH, GWT]);
}

defineClass(180, 8, {180:1, 3:1, 9:1, 8:1}, RuntimeType);
var BATCH, ECLIPSE_PLUGIN, GWT, STANDALONE;
var Lcom_baselet_control_enums_RuntimeType_2_classLit = createForEnum('com.baselet.control.enums', 'RuntimeType', 180, values_8);
function $clinit_ColorOwn(){
  $clinit_ColorOwn = emptyMethod;
  getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_diagram_draw_helper_ColorOwn_2_classLit) , Lcom_baselet_diagram_draw_helper_ColorOwn_2_classLit.typeName));
}

function $equals_1(this$static, obj){
  var other;
  if (this$static === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lcom_baselet_diagram_draw_helper_ColorOwn_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = castTo(obj, 39);
  if (this$static.alpha_0 != other.alpha_0) {
    return false;
  }
  if (this$static.blue != other.blue) {
    return false;
  }
  if (this$static.green != other.green) {
    return false;
  }
  if (this$static.red != other.red) {
    return false;
  }
  return true;
}

function $hashCode_0(this$static){
  var result;
  result = 31 + this$static.alpha_0;
  result = 31 * result + this$static.blue;
  result = 31 * result + this$static.green;
  result = 31 * result + this$static.red;
  return result;
}

function $transparency(this$static, alpha_0){
  return new ColorOwn(this$static.red, this$static.green, this$static.blue, alpha_0);
}

function ColorOwn(red, green, blue, alpha_0){
  $clinit_ColorOwn();
  this.red = red;
  this.green = green;
  this.blue = blue;
  this.alpha_0 = alpha_0;
}

function ColorOwn_0(red, green, blue, transparency){
  $clinit_ColorOwn();
  ColorOwn.call(this, red, green, blue, transparency.alpha_0);
}

defineClass(39, 1, {39:1}, ColorOwn, ColorOwn_0);
_.equals_0 = function equals_10(obj){
  return $equals_1(this, obj);
}
;
_.hashCode_0 = function hashCode_11(){
  return $hashCode_0(this);
}
;
_.toString_0 = function toString_13(){
  return 'ColorOwn [red=' + this.red + ', green=' + this.green + ', blue=' + this.blue + ', alpha=' + this.alpha_0 + ']';
}
;
_.alpha_0 = 0;
_.blue = 0;
_.green = 0;
_.red = 0;
var Lcom_baselet_diagram_draw_helper_ColorOwn_2_classLit = createForClass('com.baselet.diagram.draw.helper', 'ColorOwn', 39);
function $clinit_ColorOwn$Transparency(){
  $clinit_ColorOwn$Transparency = emptyMethod;
  FOREGROUND = new ColorOwn$Transparency('FOREGROUND', 0, 255);
  FULL_TRANSPARENT = new ColorOwn$Transparency('FULL_TRANSPARENT', 1, 0);
  DEPRECATED_WARNING = new ColorOwn$Transparency('DEPRECATED_WARNING', 2, 175);
  BACKGROUND = new ColorOwn$Transparency('BACKGROUND', 3, 125);
  SELECTION_BACKGROUND = new ColorOwn$Transparency('SELECTION_BACKGROUND', 4, 20);
}

function ColorOwn$Transparency(enum$name, enum$ordinal, alpha_0){
  Enum.call(this, enum$name, enum$ordinal);
  this.alpha_0 = alpha_0;
}

function values_9(){
  $clinit_ColorOwn$Transparency();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_diagram_draw_helper_ColorOwn$Transparency_2_classLit, 1), $intern_3, 155, 0, [FOREGROUND, FULL_TRANSPARENT, DEPRECATED_WARNING, BACKGROUND, SELECTION_BACKGROUND]);
}

defineClass(155, 8, {155:1, 3:1, 9:1, 8:1}, ColorOwn$Transparency);
_.alpha_0 = 0;
var BACKGROUND, DEPRECATED_WARNING, FOREGROUND, FULL_TRANSPARENT, SELECTION_BACKGROUND;
var Lcom_baselet_diagram_draw_helper_ColorOwn$Transparency_2_classLit = createForEnum('com.baselet.diagram.draw.helper', 'ColorOwn/Transparency', 155, values_9);
function $$init(this$static){
  this$static.stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_4, 126, 0, 0, 1);
}

function $addSuppressed(this$static, exception){
  checkCriticalNotNull_0(exception, 'Cannot suppress a null exception.');
  checkCriticalArgument(exception != this$static, 'Exception can not suppress itself.');
  if (this$static.disableSuppression) {
    return;
  }
  this$static.suppressedExceptions == null?(this$static.suppressedExceptions = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Throwable_2_classLit, 1), $intern_4, 17, 0, [exception])):(this$static.suppressedExceptions[this$static.suppressedExceptions.length] = exception);
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
  for (t$array = (this$static.suppressedExceptions == null && (this$static.suppressedExceptions = initUnidimensionalArray(Ljava_lang_Throwable_2_classLit, $intern_4, 17, 0, 0, 1)) , this$static.suppressedExceptions) , t$index = 0 , t$max = t$array.length; t$index < t$max; ++t$index) {
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

defineClass(17, 1, {3:1, 17:1});
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

defineClass(13, 17, {3:1, 13:1, 17:1}, Exception_0);
var Ljava_lang_Exception_2_classLit = createForClass('java.lang', 'Exception', 13);
function RuntimeException(){
  Exception_0.call(this);
}

function RuntimeException_0(message){
  Exception_1.call(this, message);
}

function RuntimeException_1(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(10, 13, $intern_5, RuntimeException, RuntimeException_0, RuntimeException_1);
var Ljava_lang_RuntimeException_2_classLit = createForClass('java.lang', 'RuntimeException', 10);
function $getColor(this$static, colorStyle){
  return castTo($get_8(this$static.styleColorMap, colorStyle), 39);
}

defineClass(1041, 1, {});
var Lcom_baselet_diagram_draw_helper_theme_Theme_2_classLit = createForClass('com.baselet.diagram.draw.helper.theme', 'Theme', 1041);
function $clinit_Theme$ColorStyle(){
  $clinit_Theme$ColorStyle = emptyMethod;
  SELECTION_FG = new Theme$ColorStyle('SELECTION_FG', 0);
  SELECTION_BG = new Theme$ColorStyle('SELECTION_BG', 1);
  STICKING_POLYGON = new Theme$ColorStyle('STICKING_POLYGON', 2);
  SYNTAX_HIGHLIGHTING = new Theme$ColorStyle('SYNTAX_HIGHLIGHTING', 3);
  DEFAULT_FOREGROUND = new Theme$ColorStyle('DEFAULT_FOREGROUND', 4);
  DEFAULT_BACKGROUND = new Theme$ColorStyle('DEFAULT_BACKGROUND', 5);
  DEFAULT_DOCUMENT_BACKGROUND = new Theme$ColorStyle('DEFAULT_DOCUMENT_BACKGROUND', 6);
  DEFAULT_SPLITTER_COLOR = new Theme$ColorStyle('DEFAULT_SPLITTER_COLOR', 7);
}

function Theme$ColorStyle(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_10(){
  $clinit_Theme$ColorStyle();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_diagram_draw_helper_theme_Theme$ColorStyle_2_classLit, 1), $intern_3, 119, 0, [SELECTION_FG, SELECTION_BG, STICKING_POLYGON, SYNTAX_HIGHLIGHTING, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND, DEFAULT_DOCUMENT_BACKGROUND, DEFAULT_SPLITTER_COLOR]);
}

defineClass(119, 8, {119:1, 3:1, 9:1, 8:1}, Theme$ColorStyle);
var DEFAULT_BACKGROUND, DEFAULT_DOCUMENT_BACKGROUND, DEFAULT_FOREGROUND, DEFAULT_SPLITTER_COLOR, SELECTION_BG, SELECTION_FG, STICKING_POLYGON, SYNTAX_HIGHLIGHTING;
var Lcom_baselet_diagram_draw_helper_theme_Theme$ColorStyle_2_classLit = createForEnum('com.baselet.diagram.draw.helper.theme', 'Theme/ColorStyle', 119, values_10);
function $clinit_Theme$PredefinedColors(){
  $clinit_Theme$PredefinedColors = emptyMethod;
  RED = new Theme$PredefinedColors('RED', 0);
  GREEN = new Theme$PredefinedColors('GREEN', 1);
  BLUE = new Theme$PredefinedColors('BLUE', 2);
  YELLOW = new Theme$PredefinedColors('YELLOW', 3);
  MAGENTA = new Theme$PredefinedColors('MAGENTA', 4);
  WHITE = new Theme$PredefinedColors('WHITE', 5);
  BLACK = new Theme$PredefinedColors('BLACK', 6);
  ORANGE = new Theme$PredefinedColors('ORANGE', 7);
  CYAN = new Theme$PredefinedColors('CYAN', 8);
  DARK_GRAY = new Theme$PredefinedColors('DARK_GRAY', 9);
  GRAY = new Theme$PredefinedColors('GRAY', 10);
  LIGHT_GRAY = new Theme$PredefinedColors('LIGHT_GRAY', 11);
  PINK = new Theme$PredefinedColors('PINK', 12);
  TRANSPARENT = new Theme$PredefinedColors('TRANSPARENT', 13);
  NONE = new Theme$PredefinedColors('NONE', 14);
}

function Theme$PredefinedColors(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_11(){
  $clinit_Theme$PredefinedColors();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_diagram_draw_helper_theme_Theme$PredefinedColors_2_classLit, 1), $intern_3, 79, 0, [RED, GREEN, BLUE, YELLOW, MAGENTA, WHITE, BLACK, ORANGE, CYAN, DARK_GRAY, GRAY, LIGHT_GRAY, PINK, TRANSPARENT, NONE]);
}

defineClass(79, 8, {79:1, 3:1, 9:1, 8:1}, Theme$PredefinedColors);
var BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, NONE, ORANGE, PINK, RED, TRANSPARENT, WHITE, YELLOW;
var Lcom_baselet_diagram_draw_helper_theme_Theme$PredefinedColors_2_classLit = createForEnum('com.baselet.diagram.draw.helper.theme', 'Theme/PredefinedColors', 79, values_11);
function $clinit_ThemeDark(){
  $clinit_ThemeDark = emptyMethod;
  getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_diagram_draw_helper_theme_ThemeDark_2_classLit) , Lcom_baselet_diagram_draw_helper_theme_ThemeDark_2_classLit.typeName));
}

function $generateColorMaps(this$static){
  var colorMap, styleColorMap;
  colorMap = new HashMap;
  $put(colorMap, ($clinit_Theme$PredefinedColors() , BLACK), this$static.BLACK);
  $put(colorMap, BLUE, this$static.BLUE);
  $put(colorMap, CYAN, this$static.CYAN);
  $put(colorMap, DARK_GRAY, this$static.DARK_GRAY);
  $put(colorMap, GRAY, this$static.GRAY);
  $put(colorMap, GREEN, this$static.GREEN);
  $put(colorMap, LIGHT_GRAY, this$static.LIGHT_GRAY);
  $put(colorMap, MAGENTA, this$static.MAGENTA);
  $put(colorMap, ORANGE, this$static.ORANGE);
  $put(colorMap, PINK, this$static.PINK);
  $put(colorMap, RED, this$static.RED);
  $put(colorMap, WHITE, this$static.WHITE);
  $put(colorMap, YELLOW, this$static.YELLOW);
  $put(colorMap, TRANSPARENT, this$static.TRANSPARENT);
  $put(colorMap, NONE, this$static.TRANSPARENT);
  this$static.colorMap = ($clinit_Collections() , new Collections$UnmodifiableMap(colorMap));
  styleColorMap = new HashMap;
  $put(styleColorMap, ($clinit_Theme$ColorStyle() , SELECTION_FG), this$static.SELECTION_FG);
  $put(styleColorMap, SELECTION_BG, this$static.SELECTION_BG);
  $put(styleColorMap, STICKING_POLYGON, this$static.STICKING_POLYGON);
  $put(styleColorMap, SYNTAX_HIGHLIGHTING, this$static.SYNTAX_HIGHLIGHTING);
  $put(styleColorMap, DEFAULT_FOREGROUND, this$static.DEFAULT_FOREGROUND);
  $put(styleColorMap, DEFAULT_BACKGROUND, this$static.DEFAULT_BACKGROUND);
  $put(styleColorMap, DEFAULT_SPLITTER_COLOR, this$static.DEFAULT_SPLITTER_COLOR);
  $put(styleColorMap, DEFAULT_DOCUMENT_BACKGROUND, this$static.DEFAULT_DOCUMENT_BACKGROUND);
  this$static.styleColorMap = new Collections$UnmodifiableMap(styleColorMap);
}

function ThemeDark(){
  $clinit_ThemeDark();
  this.RED = new ColorOwn_0(220, 0, 0, ($clinit_ColorOwn$Transparency() , FOREGROUND));
  this.GREEN = new ColorOwn_0(0, 220, 0, FOREGROUND);
  this.BLUE = new ColorOwn_0(0, 0, 220, FOREGROUND);
  this.YELLOW = new ColorOwn_0(100, 100, 0, FOREGROUND);
  this.MAGENTA = new ColorOwn_0(100, 0, 100, FOREGROUND);
  this.WHITE = new ColorOwn_0(255, 255, 255, FOREGROUND);
  this.BLACK = new ColorOwn_0(40, 40, 40, FOREGROUND);
  this.ORANGE = new ColorOwn_0(175, 117, 0, FOREGROUND);
  this.CYAN = new ColorOwn_0(0, 100, 100, FOREGROUND);
  this.DARK_GRAY = new ColorOwn_0(70, 70, 70, FOREGROUND);
  this.GRAY = new ColorOwn_0(120, 120, 120, FOREGROUND);
  this.LIGHT_GRAY = new ColorOwn_0(200, 200, 200, FOREGROUND);
  this.PINK = new ColorOwn_0(205, 120, 120, FOREGROUND);
  this.TRANSPARENT = $transparency(this.BLACK, FULL_TRANSPARENT.alpha_0);
  this.SELECTION_FG = new ColorOwn_0(150, 150, 255, FOREGROUND);
  this.SELECTION_BG = new ColorOwn_0(0, 0, 255, SELECTION_BACKGROUND);
  this.STICKING_POLYGON = new ColorOwn_0(100, 180, 255, FOREGROUND);
  this.SYNTAX_HIGHLIGHTING = new ColorOwn_0(0, 100, 255, FOREGROUND);
  this.DEFAULT_FOREGROUND = this.WHITE;
  this.DEFAULT_BACKGROUND = this.TRANSPARENT;
  this.DEFAULT_DOCUMENT_BACKGROUND = this.BLACK;
  this.DEFAULT_SPLITTER_COLOR = this.GRAY;
  $generateColorMaps(this);
}

defineClass(555, 1041, {}, ThemeDark);
var Lcom_baselet_diagram_draw_helper_theme_ThemeDark_2_classLit = createForClass('com.baselet.diagram.draw.helper.theme', 'ThemeDark', 555);
function $clinit_ThemeFactory(){
  $clinit_ThemeFactory = emptyMethod;
  var e, theme;
  getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_diagram_draw_helper_theme_ThemeFactory_2_classLit) , Lcom_baselet_diagram_draw_helper_theme_ThemeFactory_2_classLit.typeName));
  listeners = new ArrayList;
  theme = null;
  try {
    $wnd.changeTheme = $entry(changeTheme_0);
    theme = $wnd.theme;
  }
   catch ($e0) {
    $e0 = toJava($e0);
    if (instanceOf($e0, 153)) {
      e = $e0;
      if (!$equals_6($getCanonicalName(e.___clazz), 'java.lang.UnsatisfiedLinkError'))
        throw toJs(e);
    }
     else 
      throw toJs($e0);
  }
  theme != null && changeTheme(($clinit_ThemeFactory$THEMES() , castTo(valueOf(($clinit_ThemeFactory$THEMES$Map() , $MAP_3), theme), 154)));
}

function changeTheme(chosenTheme){
  $clinit_ThemeFactory();
  var listener, listener$iterator;
  if (chosenTheme == activeThemeEnum) {
    return;
  }
  switch (chosenTheme.ordinal) {
    case 1:
      activeThemeEnum = chosenTheme;
      theme_0 = new ThemeDark;
      break;
    case 0:
      activeThemeEnum = chosenTheme;
      theme_0 = new ThemeLight;
      break;
    default:activeThemeEnum = ($clinit_ThemeFactory$THEMES() , LIGHT);
      theme_0 = new ThemeLight;
  }
  for (listener$iterator = new ArrayList$1(listeners); listener$iterator.i < listener$iterator.this$01.array.length;) {
    listener = castTo($next_3(listener$iterator), 230);
    listener.onThemeChange();
  }
}

function changeTheme_0(themeString){
  changeTheme(($clinit_ThemeFactory$THEMES() , castTo(valueOf(($clinit_ThemeFactory$THEMES$Map() , $MAP_3), themeString), 154)));
}

var activeThemeEnum, listeners, theme_0;
var Lcom_baselet_diagram_draw_helper_theme_ThemeFactory_2_classLit = createForClass('com.baselet.diagram.draw.helper.theme', 'ThemeFactory', null);
function $clinit_ThemeFactory$THEMES(){
  $clinit_ThemeFactory$THEMES = emptyMethod;
  LIGHT = new ThemeFactory$THEMES('LIGHT', 0);
  DARK = new ThemeFactory$THEMES('DARK', 1);
}

function ThemeFactory$THEMES(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_12(){
  $clinit_ThemeFactory$THEMES();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_diagram_draw_helper_theme_ThemeFactory$THEMES_2_classLit, 1), $intern_3, 154, 0, [LIGHT, DARK]);
}

defineClass(154, 8, {154:1, 3:1, 9:1, 8:1}, ThemeFactory$THEMES);
var DARK, LIGHT;
var Lcom_baselet_diagram_draw_helper_theme_ThemeFactory$THEMES_2_classLit = createForEnum('com.baselet.diagram.draw.helper.theme', 'ThemeFactory/THEMES', 154, values_12);
function $clinit_ThemeFactory$THEMES$Map(){
  $clinit_ThemeFactory$THEMES$Map = emptyMethod;
  $MAP_3 = createValueOfMap(($clinit_ThemeFactory$THEMES() , stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_diagram_draw_helper_theme_ThemeFactory$THEMES_2_classLit, 1), $intern_3, 154, 0, [LIGHT, DARK])));
}

var $MAP_3;
function $clinit_ThemeLight(){
  $clinit_ThemeLight = emptyMethod;
  getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_diagram_draw_helper_theme_ThemeLight_2_classLit) , Lcom_baselet_diagram_draw_helper_theme_ThemeLight_2_classLit.typeName));
}

function $generateColorMaps_0(this$static){
  var colorMap, styleColorMap;
  colorMap = new HashMap;
  $put(colorMap, ($clinit_Theme$PredefinedColors() , BLACK), this$static.BLACK);
  $put(colorMap, BLUE, this$static.BLUE);
  $put(colorMap, CYAN, this$static.CYAN);
  $put(colorMap, DARK_GRAY, this$static.DARK_GRAY);
  $put(colorMap, GRAY, this$static.GRAY);
  $put(colorMap, GREEN, this$static.GREEN);
  $put(colorMap, LIGHT_GRAY, this$static.LIGHT_GRAY);
  $put(colorMap, MAGENTA, this$static.MAGENTA);
  $put(colorMap, ORANGE, this$static.ORANGE);
  $put(colorMap, PINK, this$static.PINK);
  $put(colorMap, RED, this$static.RED);
  $put(colorMap, WHITE, this$static.WHITE);
  $put(colorMap, YELLOW, this$static.YELLOW);
  $put(colorMap, TRANSPARENT, this$static.TRANSPARENT);
  $put(colorMap, NONE, this$static.TRANSPARENT);
  this$static.colorMap = ($clinit_Collections() , new Collections$UnmodifiableMap(colorMap));
  styleColorMap = new HashMap;
  $put(styleColorMap, ($clinit_Theme$ColorStyle() , SELECTION_FG), this$static.SELECTION_FG);
  $put(styleColorMap, SELECTION_BG, this$static.SELECTION_BG);
  $put(styleColorMap, STICKING_POLYGON, this$static.STICKING_POLYGON);
  $put(styleColorMap, SYNTAX_HIGHLIGHTING, this$static.SYNTAX_HIGHLIGHTING);
  $put(styleColorMap, DEFAULT_FOREGROUND, this$static.DEFAULT_FOREGROUND);
  $put(styleColorMap, DEFAULT_BACKGROUND, this$static.DEFAULT_BACKGROUND);
  $put(styleColorMap, DEFAULT_DOCUMENT_BACKGROUND, this$static.DEFAULT_CANVAS);
  $put(styleColorMap, DEFAULT_SPLITTER_COLOR, this$static.DEFAULT_SPLITTER_COLOR);
  this$static.styleColorMap = new Collections$UnmodifiableMap(styleColorMap);
}

function ThemeLight(){
  $clinit_ThemeLight();
  this.RED = new ColorOwn_0(255, 0, 0, ($clinit_ColorOwn$Transparency() , FOREGROUND));
  this.GREEN = new ColorOwn_0(0, 255, 0, FOREGROUND);
  this.BLUE = new ColorOwn_0(0, 0, 255, FOREGROUND);
  this.YELLOW = new ColorOwn_0(255, 255, 0, FOREGROUND);
  this.MAGENTA = new ColorOwn_0(255, 0, 255, FOREGROUND);
  this.WHITE = new ColorOwn_0(255, 255, 255, FOREGROUND);
  this.BLACK = new ColorOwn_0(0, 0, 0, FOREGROUND);
  this.ORANGE = new ColorOwn_0(255, 165, 0, FOREGROUND);
  this.CYAN = new ColorOwn_0(0, 255, 255, FOREGROUND);
  this.DARK_GRAY = new ColorOwn_0(70, 70, 70, FOREGROUND);
  this.GRAY = new ColorOwn_0(120, 120, 120, FOREGROUND);
  this.LIGHT_GRAY = new ColorOwn_0(200, 200, 200, FOREGROUND);
  this.PINK = new ColorOwn_0(255, 175, 175, FOREGROUND);
  this.TRANSPARENT = $transparency(this.WHITE, FULL_TRANSPARENT.alpha_0);
  this.SELECTION_FG = this.BLUE;
  this.SELECTION_BG = new ColorOwn_0(0, 0, 255, SELECTION_BACKGROUND);
  this.STICKING_POLYGON = new ColorOwn_0(100, 180, 255, FOREGROUND);
  this.SYNTAX_HIGHLIGHTING = new ColorOwn_0(0, 100, 255, FOREGROUND);
  this.DEFAULT_FOREGROUND = this.BLACK;
  this.DEFAULT_BACKGROUND = this.TRANSPARENT;
  this.DEFAULT_CANVAS = this.WHITE;
  this.DEFAULT_SPLITTER_COLOR = new ColorOwn_0(231, 231, 231, FOREGROUND);
  $generateColorMaps_0(this);
}

defineClass(333, 1041, {}, ThemeLight);
var Lcom_baselet_diagram_draw_helper_theme_ThemeLight_2_classLit = createForClass('com.baselet.diagram.draw.helper.theme', 'ThemeLight', 333);
function $lambda$0(this$static, throwable_0){
  var unwrapped;
  unwrapped = $unwrap(this$static, throwable_0);
  showFeatureNotSupported('Sorry, the program just crashed. Please check logs and report a bug.', false);
  this$static.log_0.error_0(unwrapped.getMessage());
}

function $onModuleLoad(this$static){
  var s, i;
  setUncaughtExceptionHandler(new BaseletGWT$lambda$0$Type(this$static));
  $clinit_ThemeFactory();
  $add_13(listeners, this$static);
  $getElement(get_7()).style['backgroundColor'] = convert($getColor((!theme_0 && changeTheme(($clinit_ThemeFactory$THEMES() , LIGHT)) , theme_0), ($clinit_Theme$ColorStyle() , DEFAULT_DOCUMENT_BACKGROUND)));
  this$static.log_0.info_1('Starting GUI ...');
  init(($clinit_RuntimeType() , GWT));
  $setDev_mode(($clinit_SharedConfig() , $clinit_SharedConfig() , instance_0), getParameter('dev') != null);
  $clinit_WebStorage();
  GetVersion() == 1?(clipboardStorage = new VsCodeClipboardDEPRECATED):(clipboardStorage = new LocalStorageClipboard);
  clipboardStorage.init() || (get_1() == ($clinit_Browser() , INTERNET_EXPLORER) && $startsWith(($clinit_Impl() , s = $doc.location.href , i = s.indexOf('#') , i != -1 && (s = s.substring(0, i)) , i = s.indexOf('?') , i != -1 && (s = s.substring(0, i)) , i = s.lastIndexOf('/') , i != -1 && (s = s.substring(0, i)) , s.length > 0?s + '/':''), 'file:')?showFeatureNotSupported('You have opened this webpage from your filesystem, therefore<br/>Internet Explorer will not support local storage<br/><br/>Please use another browser like Firefox or Chrome,<br/>or open this application using the web url', false):showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'local storage' (or has cookies disabled)<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false));
  if (typeof FileReader != 'undefined') {
    showInfo('Loading application ... please wait ...');
    runAsync(1, new BaseletGWT$1);
    (null , instance_0).dev_mode || addWindowClosingHandler(new BaseletGWT$2);
  }
   else {
    showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'file reader'<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false);
  }
  this$static.log_0.info_1('GUI started');
}

function $unwrap(this$static, e){
  var ue;
  if (instanceOf(e, 135)) {
    ue = castTo(e, 135);
    if (ue.causes.size_1() == 1) {
      return $unwrap(this$static, castTo(ue.causes.iterator().next_1(), 17));
    }
  }
  return e;
}

function BaseletGWT(){
  this.log_0 = isVsCodeVersion()?new VsCodeLogger:new GWTLogger(Lcom_baselet_gwt_client_BaseletGWT_2_classLit);
}

defineClass(398, 1, {230:1}, BaseletGWT);
_.onThemeChange = function onThemeChange(){
  $getElement(get_7()).style['backgroundColor'] = convert($getColor(($clinit_ThemeFactory() , !theme_0 && changeTheme(($clinit_ThemeFactory$THEMES() , LIGHT)) , $clinit_ThemeFactory() , theme_0), ($clinit_Theme$ColorStyle() , DEFAULT_DOCUMENT_BACKGROUND)));
}
;
var Lcom_baselet_gwt_client_BaseletGWT_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT', 398);
var Lcom_google_gwt_core_client_RunAsyncCallback_2_classLit = createForInterface('com.google.gwt.core.client', 'RunAsyncCallback');
function BaseletGWT$1(){
}

defineClass(408, 1, {310:1}, BaseletGWT$1);
var Lcom_baselet_gwt_client_BaseletGWT$1_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT/1', 408);
function BaseletGWT$2(){
}

defineClass(409, 1, {26:1, 1017:1}, BaseletGWT$2);
var Lcom_baselet_gwt_client_BaseletGWT$2_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT/2', 409);
function BaseletGWT$lambda$0$Type($$outer_0){
  this.$$outer_0 = $$outer_0;
}

defineClass(405, 1, {}, BaseletGWT$lambda$0$Type);
_.onUncaughtException = function onUncaughtException(arg0){
  $lambda$0(this.$$outer_0, arg0);
}
;
var Lcom_baselet_gwt_client_BaseletGWT$lambda$0$Type_2_classLit = createForClass('com.baselet.gwt.client', 'BaseletGWT/lambda$0$Type', 405);
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
  for (b$array = stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_gwt_client_base_Browser_2_classLit, 1), $intern_3, 128, 0, [INTERNET_EXPLORER, FIREFOX, CHROME, OPERA, ANDROID_STOCK_BROWSER, UNKNOWN]) , b$index = 0 , b$max = b$array.length; b$index < b$max; ++b$index) {
    b = b$array[b$index];
    if (browserFiltersMatch(currentAgent, b)) {
      return b;
    }
  }
  return UNKNOWN;
}

function values_26(){
  $clinit_Browser();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_baselet_gwt_client_base_Browser_2_classLit, 1), $intern_3, 128, 0, [INTERNET_EXPLORER, FIREFOX, CHROME, OPERA, ANDROID_STOCK_BROWSER, UNKNOWN]);
}

defineClass(128, 8, {128:1, 3:1, 9:1, 8:1}, Browser);
var ANDROID_STOCK_BROWSER, CHROME, FIREFOX, INTERNET_EXPLORER, OPERA, UNKNOWN;
var Lcom_baselet_gwt_client_base_Browser_2_classLit = createForEnum('com.baselet.gwt.client.base', 'Browser', 128, values_26);
function convert(in_0){
  if (!in_0) {
    return null;
  }
  return 'rgba(' + in_0.red + ', ' + in_0.green + ',' + in_0.blue + ', ' + in_0.alpha_0 / 255 + ')';
}

function $clinit_Notification(){
  $clinit_Notification = emptyMethod;
  element_0 = $getElement(get_8('featurewarning'));
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

defineClass(181, 1, {});
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
var Lcom_google_gwt_user_client_Timer_2_classLit = createForClass('com.google.gwt.user.client', 'Timer', 181);
function Notification$ElementFader$1(val$element, val$totalTimeMillis){
  this.val$element1 = val$element;
  this.val$startOpacity2 = 1;
  this.val$endOpacity3 = 0;
  this.val$totalTimeMillis4 = val$totalTimeMillis;
  Timer_0.call(this);
}

defineClass(436, 181, {}, Notification$ElementFader$1);
_.run = function run_11(){
  fade(this.val$element1, this.val$startOpacity2, this.val$endOpacity3, this.val$totalTimeMillis4);
}
;
_.val$endOpacity3 = 0;
_.val$startOpacity2 = 0;
_.val$totalTimeMillis4 = 0;
var Lcom_baselet_gwt_client_base_Notification$ElementFader$1_2_classLit = createForClass('com.baselet.gwt.client.base', 'Notification/ElementFader/1', 436);
function Notification$ElementFader$2(val$startOpacity, val$deltaOpacity, val$element, val$endOpacity){
  this.val$startOpacity1 = val$startOpacity;
  this.val$deltaOpacity2 = val$deltaOpacity;
  this.val$element3 = val$element;
  this.val$endOpacity4 = val$endOpacity;
  Timer_0.call(this);
}

defineClass(437, 181, {}, Notification$ElementFader$2);
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
var Lcom_baselet_gwt_client_base_Notification$ElementFader$2_2_classLit = createForClass('com.baselet.gwt.client.base', 'Notification/ElementFader/2', 437);
function LocalStorageClipboard(){
}

defineClass(407, 1, {}, LocalStorageClipboard);
_.init = function init_0(){
  try {
    localStorage_0 = (!localStorage_1 && ($clinit_Storage$StorageSupportDetector() , localStorageSupported) && (localStorage_1 = new Storage_0) , localStorage_1);
    return !!localStorage_0;
  }
   catch ($e0) {
    $e0 = toJava($e0);
    if (instanceOf($e0, 13)) {
      return false;
    }
     else 
      throw toJs($e0);
  }
}
;
var localStorage_0;
var Lcom_baselet_gwt_client_clipboard_LocalStorageClipboard_2_classLit = createForClass('com.baselet.gwt.client.clipboard', 'LocalStorageClipboard', 407);
function VsCodeClipboardDEPRECATED(){
}

defineClass(406, 1, {}, VsCodeClipboardDEPRECATED);
_.init = function init_1(){
  this.clipboard = new HashMap;
  return true;
}
;
var Lcom_baselet_gwt_client_clipboard_VsCodeClipboardDEPRECATED_2_classLit = createForClass('com.baselet.gwt.client.clipboard', 'VsCodeClipboardDEPRECATED', 406);
function $clinit_WebStorage(){
  $clinit_WebStorage = emptyMethod;
  log_13 = getLogger_0(($ensureNamesAreInitialized(Lcom_baselet_gwt_client_BaseletGWT_2_classLit) , Lcom_baselet_gwt_client_BaseletGWT_2_classLit.typeName));
}

var clipboardStorage, log_13;
function GWTLogger(clazz){
  this.logger = getLogger_0(($ensureNamesAreInitialized(clazz) , clazz.typeName));
}

defineClass(175, 1, {}, GWTLogger);
_.error_0 = function error_0(message){
  $error(this.logger, message);
}
;
_.info_1 = function info_0(message){
  $info(this.logger, message);
}
;
var Lcom_baselet_gwt_client_logging_GWTLogger_2_classLit = createForClass('com.baselet.gwt.client.logging', 'GWTLogger', 175);
function $postLog(message){
  window.parent.vscode.postMessage({command:'postLog', text:message});
}

function VsCodeLogger(){
  this.levelValue = $getLevel_0(getLogger('')).intValue();
}

defineClass(174, 1, {}, VsCodeLogger);
_.error_0 = function error_2(message){
  var date;
  (this.levelValue == ($clinit_Level() , $intern_7) || this.levelValue <= $intern_8) && $postLog((date = new Date_0 , 'UMLet|' + $format(($clinit_DateTimeFormat_0() , getFormat('yyyy-MM-dd HH:mm:ss', $getDateTimeFormatInfo(($clinit_LocaleInfo() , $clinit_LocaleInfo() , instance_5)))), date, null) + '|' + 'error|' + message));
}
;
_.info_1 = function info_1(message){
  var date;
  (this.levelValue == ($clinit_Level() , $intern_7) || this.levelValue <= 800) && $postLog((date = new Date_0 , 'UMLet|' + $format(($clinit_DateTimeFormat_0() , getFormat('yyyy-MM-dd HH:mm:ss', $getDateTimeFormatInfo(($clinit_LocaleInfo() , $clinit_LocaleInfo() , instance_5)))), date, null) + '|' + 'info|' + message));
}
;
_.levelValue = 0;
var Lcom_baselet_gwt_client_logging_VsCodeLogger_2_classLit = createForClass('com.baselet.gwt.client.logging', 'VsCodeLogger', 174);
function $getElement(this$static){
  return $clinit_DOM() , this$static.element;
}

function $setElement(this$static, elem){
  $setElement_0(this$static, ($clinit_DOM() , elem));
}

function $setElement_0(this$static, elem){
  this$static.element = elem;
}

defineClass(23, 1, $intern_9);
_.getElement = function getElement(){
  return $getElement(this);
}
;
_.toString_0 = function toString_31(){
  if (!this.element) {
    return '(null handle)';
  }
  return $getString(this.getElement());
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
  switch ($clinit_DOM() , $eventGetTypeInt(($clinit_DOMImpl() , event_0).type)) {
    case 16:
    case 32:
      related = event_0.relatedTarget;
      if (!!related && $isOrHasChild(this$static.getElement(), related)) {
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

function $removeFromParent(this$static){
  if (!this$static.parent_0) {
    $clinit_RootPanel();
    $contains_4(widgetsToDetach, this$static) && detachNow(this$static);
  }
   else if (instanceOf(this$static.parent_0, 52)) {
    castTo(this$static.parent_0, 52).remove_2(this$static);
  }
   else if (this$static.parent_0) {
    throw toJs(new IllegalStateException_0("This widget's parent does not implement HasWidgets"));
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

defineClass(24, 23, $intern_10);
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
defineClass(1036, 24, $intern_11);
_.doAttachChildren = function doAttachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , attachCommand));
}
;
_.doDetachChildren = function doDetachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , detachCommand));
}
;
var Lcom_google_gwt_user_client_ui_Panel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Panel', 1036);
function $add_4(this$static, child, container){
  $removeFromParent(child);
  $add_10(this$static.children, child);
  $clinit_DOM();
  $appendChild(container, resolve(child.getElement()));
  $setParent(child, this$static);
}

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
    $removeChild(($clinit_DOM() , $clinit_DOM() , $getParentElement_0(($clinit_DOMImpl() , elem))), elem);
    $remove_8(this$static.children, w);
  }
  return true;
}

function ComplexPanel(){
  this.children = new WidgetCollection(this);
}

defineClass(203, 1036, $intern_11);
_.iterator = function iterator_2(){
  return new WidgetCollection$WidgetIterator(this.children);
}
;
_.remove_2 = function remove_5(w){
  return $remove_0(this, w);
}
;
var Lcom_google_gwt_user_client_ui_ComplexPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'ComplexPanel', 203);
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

defineClass(1019, 1, {});
_.toString_0 = function toString_32(){
  return 'An event type';
}
;
var Lcom_google_web_bindery_event_shared_Event_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event', 1019);
function $overrideSource(this$static, source){
  this$static.source = source;
}

defineClass(1018, 1019, {});
_.revive = function revive(){
  this.dead = false;
  this.source = null;
}
;
_.dead = false;
var Lcom_google_gwt_event_shared_GwtEvent_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent', 1018);
function fireNativeEvent(nativeEvent, handlerSource, relativeElem){
  var currentNative, currentRelativeElem, type_0, type$iterator, types;
  if (registered) {
    types = castTo(registered.unsafeGet(($clinit_DOMImpl() , nativeEvent).type), 41);
    if (types) {
      for (type$iterator = types.iterator(); type$iterator.hasNext_0();) {
        type_0 = castTo(type$iterator.next_1(), 54);
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
  ourValue = this$static.get_2(key);
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
    entry = castTo(iter.next_1(), 45);
    k = entry.getKey();
    if (maskUndefined(key) === maskUndefined(k) || key != null && equals_Ljava_lang_Object__Z__devirtual$(key, k)) {
      if (remove) {
        entry = new AbstractMap$SimpleEntry(entry.getKey(), entry.getValue_0());
        iter.remove_4();
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

defineClass(1038, 1, $intern_12);
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
  if (!instanceOf(obj, 82)) {
    return false;
  }
  otherMap = castTo(obj, 82);
  if (this.size_1() != otherMap.size_1()) {
    return false;
  }
  for (entry$iterator = otherMap.entrySet_0().iterator(); entry$iterator.hasNext_0();) {
    entry = castTo(entry$iterator.next_1(), 45);
    if (!this.containsEntry(entry)) {
      return false;
    }
  }
  return true;
}
;
_.get_2 = function get_4(key){
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
    entry = castTo(entry$iterator.next_1(), 45);
    $add_17(joiner, $toString_4(this, entry.getKey()) + '=' + $toString_4(this, entry.getValue_0()));
  }
  return !joiner.builder?joiner.emptyValue:joiner.suffix.length == 0?joiner.builder.string:joiner.builder.string + ('' + joiner.suffix);
}
;
var Ljava_util_AbstractMap_2_classLit = createForClass('java.util', 'AbstractMap', 1038);
function $containsKey_0(this$static, key){
  return instanceOfString(key)?$hasStringValue(this$static, key):!!$getEntry(this$static.hashCodeMap, key);
}

function $get_0(this$static, key){
  return instanceOfString(key)?$getStringValue(this$static, key):getEntryValueOrNull($getEntry(this$static.hashCodeMap, key));
}

function $getStringValue(this$static, key){
  return key == null?getEntryValueOrNull($getEntry(this$static.hashCodeMap, null)):$get_9(this$static.stringMap, key);
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

function $remove_2(this$static, key){
  return instanceOfString(key)?$removeStringValue(this$static, key):$remove_16(this$static.hashCodeMap, key);
}

function $removeStringValue(this$static, key){
  return key == null?$remove_16(this$static.hashCodeMap, null):$remove_17(this$static.stringMap, key);
}

function $reset_0(this$static){
  this$static.hashCodeMap = new InternalHashCodeMap(this$static);
  this$static.stringMap = new InternalStringMap(this$static);
  structureChanged(this$static);
}

function $size(this$static){
  return this$static.hashCodeMap.size_0 + this$static.stringMap.size_0;
}

defineClass(234, 1038, $intern_12);
_.containsKey = function containsKey_0(key){
  return $containsKey_0(this, key);
}
;
_.entrySet_0 = function entrySet(){
  return new AbstractHashMap$EntrySet(this);
}
;
_.get_2 = function get_5(key){
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
var Ljava_util_AbstractHashMap_2_classLit = createForClass('java.util', 'AbstractHashMap', 234);
function HashMap(){
  $reset_0(this);
}

defineClass(36, 234, $intern_13, HashMap);
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
var Ljava_util_HashMap_2_classLit = createForClass('java.util', 'HashMap', 36);
function $cancel_0(this$static){
  if (!this$static.isRunning) {
    return;
  }
  this$static.wasStarted = this$static.isStarted;
  this$static.element = null;
  this$static.isRunning = false;
  this$static.isStarted = false;
  if (this$static.requestHandle) {
    this$static.requestHandle.cancel_0();
    this$static.requestHandle = null;
  }
  this$static.onCancel();
}

function $run(this$static, duration, startTime, element){
  $cancel_0(this$static);
  this$static.isRunning = true;
  this$static.isStarted = false;
  this$static.duration = duration;
  this$static.startTime = startTime;
  this$static.element = element;
  ++this$static.runId;
  $execute(this$static.callback, now_1());
}

function $run_0(this$static, duration, element){
  $run(this$static, duration, now_1(), element);
}

function $update(this$static, curTime){
  var curRunId, finished, progress;
  curRunId = this$static.runId;
  finished = curTime >= this$static.startTime + this$static.duration;
  if (this$static.isStarted && !finished) {
    progress = (curTime - this$static.startTime) / this$static.duration;
    this$static.onUpdate((1 + $wnd.Math.cos($intern_14 + progress * $intern_14)) / 2);
    return this$static.isRunning && this$static.runId == curRunId;
  }
  if (!this$static.isStarted && curTime >= this$static.startTime) {
    this$static.isStarted = true;
    this$static.onStart();
    if (!(this$static.isRunning && this$static.runId == curRunId)) {
      return false;
    }
  }
  if (finished) {
    this$static.isRunning = false;
    this$static.isStarted = false;
    this$static.onComplete();
    return false;
  }
  return true;
}

function Animation(){
  Animation_0.call(this, (!instance_4 && (instance_4 = !!$wnd.requestAnimationFrame && !!$wnd.cancelAnimationFrame?new AnimationSchedulerImplStandard:new AnimationSchedulerImplTimer) , instance_4));
}

function Animation_0(scheduler){
  this.callback = new Animation$1(this);
  this.scheduler = scheduler;
}

defineClass(263, 1, {});
_.onCancel = function onCancel(){
  this.wasStarted && this.onComplete();
}
;
_.onComplete = function onComplete(){
  this.onUpdate((1 + $wnd.Math.cos($intern_15)) / 2);
}
;
_.onStart = function onStart(){
  this.onUpdate((1 + $wnd.Math.cos($intern_14)) / 2);
}
;
_.duration = -1;
_.isRunning = false;
_.isStarted = false;
_.runId = -1;
_.startTime = -1;
_.wasStarted = false;
var Lcom_google_gwt_animation_client_Animation_2_classLit = createForClass('com.google.gwt.animation.client', 'Animation', 263);
function $execute(this$static, timestamp){
  $update(this$static.this$01, timestamp)?(this$static.this$01.requestHandle = this$static.this$01.scheduler.requestAnimationFrame_0(this$static.this$01.callback, this$static.this$01.element)):(this$static.this$01.requestHandle = null);
}

function Animation$1(this$0){
  this.this$01 = this$0;
}

defineClass(539, 1, {}, Animation$1);
_.execute_0 = function execute_22(timestamp){
  $execute(this, timestamp);
}
;
var Lcom_google_gwt_animation_client_Animation$1_2_classLit = createForClass('com.google.gwt.animation.client', 'Animation/1', 539);
defineClass(1071, 1, {});
var instance_4;
var Lcom_google_gwt_animation_client_AnimationScheduler_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationScheduler', 1071);
defineClass(288, 1, {288:1});
var Lcom_google_gwt_animation_client_AnimationScheduler$AnimationHandle_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationScheduler/AnimationHandle', 288);
function AnimationSchedulerImplStandard(){
}

function cancelImpl(holder){
  $wnd.cancelAnimationFrame(holder.id);
}

function requestImpl(cb, element){
  var callback = $entry(function(){
    var time = now_1();
    cb.execute_0(time);
  }
  );
  var handle = $wnd.requestAnimationFrame(callback, element);
  return {id:handle};
}

defineClass(817, 1071, {}, AnimationSchedulerImplStandard);
_.requestAnimationFrame_0 = function requestAnimationFrame_0(callback, element){
  var handle;
  handle = requestImpl(callback, element);
  return new AnimationSchedulerImplStandard$1(handle);
}
;
var Lcom_google_gwt_animation_client_AnimationSchedulerImplStandard_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationSchedulerImplStandard', 817);
function AnimationSchedulerImplStandard$1(val$handle){
  this.val$handle2 = val$handle;
}

defineClass(818, 288, {288:1}, AnimationSchedulerImplStandard$1);
_.cancel_0 = function cancel(){
  cancelImpl(this.val$handle2);
}
;
var Lcom_google_gwt_animation_client_AnimationSchedulerImplStandard$1_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationSchedulerImplStandard/1', 818);
function $cancelAnimationFrame(this$static, requestId){
  $remove_12(this$static.animationRequests, requestId);
  this$static.animationRequests.array.length == 0 && $cancel(this$static.timer);
}

function $updateAnimations(this$static){
  var curAnimations, duration, requestId, requestId$index, requestId$max;
  curAnimations = initUnidimensionalArray(Lcom_google_gwt_animation_client_AnimationSchedulerImplTimer$AnimationHandleImpl_2_classLit, {1014:1, 3:1, 4:1}, 289, this$static.animationRequests.array.length, 0, 1);
  curAnimations = castTo($toArray_1(this$static.animationRequests, curAnimations), 1014);
  duration = new Duration;
  for (requestId$index = 0 , requestId$max = curAnimations.length; requestId$index < requestId$max; ++requestId$index) {
    requestId = curAnimations[requestId$index];
    $remove_12(this$static.animationRequests, requestId);
    $execute(requestId.callback, duration.start_0);
  }
  this$static.animationRequests.array.length > 0 && $schedule(this$static.timer, $wnd.Math.max(5, 16 - (now_1() - duration.start_0)));
}

function AnimationSchedulerImplTimer(){
  this.animationRequests = new ArrayList;
  this.timer = new AnimationSchedulerImplTimer$1(this);
}

defineClass(819, 1071, {}, AnimationSchedulerImplTimer);
_.requestAnimationFrame_0 = function requestAnimationFrame_1(callback, element){
  var requestId;
  requestId = new AnimationSchedulerImplTimer$AnimationHandleImpl(this, callback);
  $add_13(this.animationRequests, requestId);
  this.animationRequests.array.length == 1 && $schedule(this.timer, 16);
  return requestId;
}
;
var Lcom_google_gwt_animation_client_AnimationSchedulerImplTimer_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationSchedulerImplTimer', 819);
function AnimationSchedulerImplTimer$1(this$0){
  this.this$01 = this$0;
  Timer_0.call(this);
}

defineClass(820, 181, {}, AnimationSchedulerImplTimer$1);
_.run = function run_21(){
  $updateAnimations(this.this$01);
}
;
var Lcom_google_gwt_animation_client_AnimationSchedulerImplTimer$1_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationSchedulerImplTimer/1', 820);
function AnimationSchedulerImplTimer$AnimationHandleImpl(this$0, callback){
  this.this$01 = this$0;
  this.callback = callback;
}

defineClass(289, 288, {288:1, 289:1}, AnimationSchedulerImplTimer$AnimationHandleImpl);
_.cancel_0 = function cancel_0(){
  $cancelAnimationFrame(this.this$01, this);
}
;
var Lcom_google_gwt_animation_client_AnimationSchedulerImplTimer$AnimationHandleImpl_2_classLit = createForClass('com.google.gwt.animation.client', 'AnimationSchedulerImplTimer/AnimationHandleImpl', 289);
function $getAriaValue(this$static, value_0){
  var buf, item_0, item$index, item$max;
  buf = new StringBuilder;
  for (item$index = 0 , item$max = value_0.length; item$index < item$max; ++item$index) {
    item_0 = value_0[item$index];
    $append_5($append_5(buf, this$static.getSingleValue(item_0)), ' ');
  }
  return $trim(buf.string);
}

function $set_1(this$static, element, values){
  $setAttribute(element, this$static.name_0, $getAriaValue(this$static, values));
}

function Attribute(name_0){
  this.name_0 = name_0;
}

defineClass(356, 1, {});
var Lcom_google_gwt_aria_client_Attribute_2_classLit = createForClass('com.google.gwt.aria.client', 'Attribute', 356);
function AriaValueAttribute(name_0){
  Attribute.call(this, name_0);
}

defineClass(80, 356, {}, AriaValueAttribute);
_.getSingleValue = function getSingleValue(value_0){
  return castTo(value_0, 1093).getAriaValue();
}
;
var Lcom_google_gwt_aria_client_AriaValueAttribute_2_classLit = createForClass('com.google.gwt.aria.client', 'AriaValueAttribute', 80);
function PrimitiveValueAttribute(name_0){
  Attribute.call(this, name_0);
}

defineClass(87, 356, {}, PrimitiveValueAttribute);
_.getSingleValue = function getSingleValue_0(value_0){
  return value_0 == null?'null':toString_36(value_0);
}
;
var Lcom_google_gwt_aria_client_PrimitiveValueAttribute_2_classLit = createForClass('com.google.gwt.aria.client', 'PrimitiveValueAttribute', 87);
function $clinit_State(){
  $clinit_State = emptyMethod;
  new PrimitiveValueAttribute('aria-busy');
  new AriaValueAttribute('aria-checked');
  new PrimitiveValueAttribute('aria-disabled');
  new AriaValueAttribute('aria-expanded');
  new AriaValueAttribute('aria-grabbed');
  HIDDEN = new PrimitiveValueAttribute('aria-hidden');
  new AriaValueAttribute('aria-invalid');
  new AriaValueAttribute('aria-pressed');
  new AriaValueAttribute('aria-selected');
}

var HIDDEN;
function $hashCode_2(this$static){
  return !!this$static && !!this$static.hashCode?this$static.hashCode():getHashCode_0(this$static);
}

var Lcom_google_gwt_core_client_JavaScriptObject_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptObject$', 0);
function CodeDownloadException(message){
  RuntimeException_0.call(this, message);
}

defineClass(1053, 10, $intern_5, CodeDownloadException);
var Lcom_google_gwt_core_client_CodeDownloadException_2_classLit = createForClass('com.google.gwt.core.client', 'CodeDownloadException', 1053);
function Duration(){
  this.start_0 = now_1();
}

defineClass(264, 1, {}, Duration);
_.start_0 = 0;
var Lcom_google_gwt_core_client_Duration_2_classLit = createForClass('com.google.gwt.core.client', 'Duration', 264);
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

defineClass(200, 10, $intern_5, JsException);
var Ljava_lang_JsException_2_classLit = createForClass('java.lang', 'JsException', 200);
defineClass(429, 200, $intern_5);
var Lcom_google_gwt_core_client_impl_JavaScriptExceptionBase_2_classLit = createForClass('com.google.gwt.core.client.impl', 'JavaScriptExceptionBase', 429);
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

defineClass(102, 429, {102:1, 3:1, 13:1, 10:1, 17:1}, JavaScriptException);
_.getMessage = function getMessage_2(){
  return $ensureInit(this) , this.message_0;
}
;
_.getThrown = function getThrown(){
  return maskUndefined(this.e) === maskUndefined(NOT_SET)?null:this.e;
}
;
var NOT_SET;
var Lcom_google_gwt_core_client_JavaScriptException_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptException', 102);
function now_1(){
  if (Date.now) {
    return Date.now();
  }
  return (new Date).getTime();
}

defineClass(1016, 1, {});
var Lcom_google_gwt_core_client_Scheduler_2_classLit = createForClass('com.google.gwt.core.client', 'Scheduler', 1016);
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

defineClass(272, 1, {}, ScriptInjector$FromUrl);
_.removeTag = false;
var Lcom_google_gwt_core_client_ScriptInjector$FromUrl_2_classLit = createForClass('com.google.gwt.core.client', 'ScriptInjector/FromUrl', 272);
function $clinit_AsyncFragmentLoader(){
  $clinit_AsyncFragmentLoader = emptyMethod;
  BROWSER_LOADER = new AsyncFragmentLoader(2, stampJavaTypeInfo(getClassLiteralForArray(I_classLit, 1), $intern_16, 31, 15, []), new ScriptTagLoadingStrategy);
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
        castTo(callback, 310).onSuccess();
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
  callbacks == null && (callbacks = this$static.allCallbacks[fragment] = initUnidimensionalArray(Lcom_google_gwt_core_client_RunAsyncCallback_2_classLit, $intern_1, 310, 0, 0, 1));
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
  this.allCallbacks = initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_4, 4, numEntriesPlusOne, 3, 2);
  this.requestedExclusives = new AsyncFragmentLoader$BoundedIntQueue(numEntriesPlusOne);
  this.isLoaded = initUnidimensionalArray(Z_classLit, $intern_17, 31, numEntriesPlusOne, 16, 1);
  this.pendingDownloadErrorHandlers = initUnidimensionalArray(Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit, $intern_1, 314, numEntriesPlusOne, 0, 1);
}

function onLoad_1(fragment){
  $clinit_AsyncFragmentLoader();
  $onLoadImpl(BROWSER_LOADER, fragment);
}

function runAsync(fragment, callback){
  $clinit_AsyncFragmentLoader();
  $runAsyncImpl(BROWSER_LOADER, fragment, callback);
}

defineClass(418, 1, {}, AsyncFragmentLoader);
_.fragmentLoading = -1;
_.numEntries = 0;
_.remainingInitialFragments = null;
var BROWSER_LOADER;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader', 418);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit = createForInterface('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/LoadTerminatedHandler');
function AsyncFragmentLoader$1(this$0, val$fragment){
  this.this$01 = this$0;
  this.val$fragment2 = val$fragment;
}

defineClass(420, 1, {314:1}, AsyncFragmentLoader$1);
_.loadTerminated = function loadTerminated(reason){
  var callback, callback$index, callback$max, callbacks;
  callbacks = this.this$01.allCallbacks[this.val$fragment2];
  if (callbacks != null) {
    this.this$01.allCallbacks[this.val$fragment2] = null;
    for (callback$index = 0 , callback$max = callbacks.length; callback$index < callback$max; ++callback$index) {
      callback = callbacks[callback$index];
      castTo(callback, 310);
      showFeatureNotSupported('Cannot load application from server', false);
    }
  }
}
;
_.val$fragment2 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/1', 420);
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
  this.array = initUnidimensionalArray(I_classLit, $intern_16, 31, maxPuts, 15, 1);
}

defineClass(313, 1, {}, AsyncFragmentLoader$BoundedIntQueue);
_.read = 0;
_.write_0 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$BoundedIntQueue_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/BoundedIntQueue', 313);
function AsyncFragmentLoader$HttpDownloadFailure(url_0){
  RuntimeException_0.call(this, 'Download of ' + url_0 + ' failed with status ' + 404 + '(' + 'Script Tag Failure - no status available' + ')');
}

defineClass(422, 10, $intern_5, AsyncFragmentLoader$HttpDownloadFailure);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$HttpDownloadFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/HttpDownloadFailure', 422);
function AsyncFragmentLoader$HttpInstallFailure(url_0, text_0, rootCause){
  RuntimeException_1.call(this, 'Install of ' + url_0 + ' failed with text ' + text_0, rootCause);
}

defineClass(423, 10, $intern_5, AsyncFragmentLoader$HttpInstallFailure);
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$HttpInstallFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/HttpInstallFailure', 423);
function $loadTerminated(this$static, reason){
  var e, handler, handler$array, handler$index, handler$max, handlersToRun, lastException;
  if (this$static.this$01.fragmentLoading != this$static.fragment_0) {
    return;
  }
  handlersToRun = this$static.this$01.pendingDownloadErrorHandlers;
  this$static.this$01.pendingDownloadErrorHandlers = initUnidimensionalArray(Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$LoadTerminatedHandler_2_classLit, $intern_1, 314, this$static.this$01.numEntries + 1, 0, 1);
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

defineClass(421, 1, {314:1}, AsyncFragmentLoader$ResetAfterDownloadFailure);
_.loadTerminated = function loadTerminated_0(reason){
  $loadTerminated(this, reason);
}
;
_.fragment_0 = 0;
var Lcom_google_gwt_core_client_impl_AsyncFragmentLoader$ResetAfterDownloadFailure_2_classLit = createForClass('com.google.gwt.core.client.impl', 'AsyncFragmentLoader/ResetAfterDownloadFailure', 421);
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
    handler.onUncaughtException(e);
    return;
  }
  if (reportSwallowedExceptionToBrowser) {
    reportToBrowser(instanceOf(e, 102)?castTo(e, 102).getThrown():e);
  }
   else {
    $clinit_System();
    $printStackTraceImpl(e, err_0, '', '');
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

defineClass(432, 1, {});
var MAX_AUTO_RETRY_COUNT = 3;
var Lcom_google_gwt_core_client_impl_LoadingStrategyBase_2_classLit = createForClass('com.google.gwt.core.client.impl', 'LoadingStrategyBase', 432);
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

defineClass(433, 1, {}, LoadingStrategyBase$RequestData);
_.errorHandler = null;
_.fragment_0 = 0;
_.maxRetryCount = 0;
_.retryCount = 0;
var Lcom_google_gwt_core_client_impl_LoadingStrategyBase$RequestData_2_classLit = createForClass('com.google.gwt.core.client.impl', 'LoadingStrategyBase/RequestData', 433);
function OnSuccessExecutor$1(val$callback){
  this.val$callback3 = val$callback;
}

defineClass(419, 1, {}, OnSuccessExecutor$1);
_.execute = function execute_23(){
  this.val$callback3.onSuccess();
}
;
var Lcom_google_gwt_core_client_impl_OnSuccessExecutor$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'OnSuccessExecutor/1', 419);
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

function execute_24(cmd){
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
    var ret = $entry(execute_24)(cmd);
    ret && $wnd.setTimeout(callback, delayMs);
  }

  $wnd.setTimeout(callback, delayMs);
}

defineClass(510, 1016, {}, SchedulerImpl);
_.flushRunning = false;
_.shouldBeRunning = false;
var INSTANCE_29;
var Lcom_google_gwt_core_client_impl_SchedulerImpl_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl', 510);
function SchedulerImpl$Flusher(this$0){
  this.this$01 = this$0;
}

defineClass(511, 1, {}, SchedulerImpl$Flusher);
_.execute_1 = function execute_25(){
  this.this$01.flushRunning = true;
  $flushPostEventPumpCommands(this.this$01);
  this.this$01.flushRunning = false;
  return this.this$01.shouldBeRunning = $isWorkQueued(this.this$01);
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Flusher_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Flusher', 511);
function SchedulerImpl$Rescuer(this$0){
  this.this$01 = this$0;
}

defineClass(512, 1, {}, SchedulerImpl$Rescuer);
_.execute_1 = function execute_26(){
  this.this$01.flushRunning && scheduleFixedDelayImpl(this.this$01.flusher, 1);
  return this.this$01.shouldBeRunning;
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Rescuer_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Rescuer', 512);
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

defineClass(417, 432, {}, ScriptTagLoadingStrategy);
var Lcom_google_gwt_core_client_impl_ScriptTagLoadingStrategy_2_classLit = createForClass('com.google.gwt.core.client.impl', 'ScriptTagLoadingStrategy', 417);
function ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1(val$request){
  this.val$request2 = val$request;
}

defineClass(319, 1, {}, ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1);
_.onFailure = function onFailure(reason){
  var lastArg;
  cleanup((lastArg = this , castTo(reason, 13) , lastArg).val$request2);
}
;
_.onSuccess_0 = function onSuccess_0(result){
  var lastArg;
  cleanup((lastArg = this , throwClassCastExceptionUnlessNull(result) , lastArg).val$request2);
}
;
var Lcom_google_gwt_core_client_impl_ScriptTagLoadingStrategy$ScriptTagDownloadStrategy$1_2_classLit = createForClass('com.google.gwt.core.client.impl', 'ScriptTagLoadingStrategy/ScriptTagDownloadStrategy/1', 319);
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
defineClass(1031, 1, {});
var Lcom_google_gwt_core_client_impl_StackTraceCreator$Collector_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/Collector', 1031);
function StackTraceCreator$CollectorLegacy(){
}

defineClass(430, 1031, {}, StackTraceCreator$CollectorLegacy);
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
  stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_4, 126, length_0, 0, 1);
  for (i = 0; i < length_0; i++) {
    stackTrace[i] = new StackTraceElement(stack_0[i], null, -1);
  }
  return stackTrace;
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorLegacy_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorLegacy', 430);
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

defineClass(1032, 1031, {});
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
  stackTrace = initUnidimensionalArray(Ljava_lang_StackTraceElement_2_classLit, $intern_4, 126, 0, 0, 1);
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
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModern_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModern', 1032);
function StackTraceCreator$CollectorModernNoSourceMap(){
}

defineClass(431, 1032, {}, StackTraceCreator$CollectorModernNoSourceMap);
_.createSte = function createSte_0(fileName, method, line, col){
  return new StackTraceElement(method, fileName, -1);
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModernNoSourceMap_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModernNoSourceMap', 431);
function $appendChild(this$static, newChild){
  return this$static.appendChild(newChild);
}

function $isOrHasChild(this$static, child){
  return $isOrHasChild_0(($clinit_DOMImpl() , this$static), child);
}

function $removeChild(this$static, oldChild){
  return this$static.removeChild(oldChild);
}

function $getString(this$static){
  return ($clinit_DOMImpl() , this$static).outerHTML;
}

function $setAttribute(this$static, name_0, value_0){
  this$static.setAttribute(name_0, value_0);
}

function $setInnerHTML(this$static, html){
  this$static.innerHTML = html || '';
}

function $clinit_DOMImpl(){
  $clinit_DOMImpl = emptyMethod;
  impl_1 = new DOMImplWebkit;
}

function $getParentElement_0(node){
  var parent_0 = node.parentNode;
  (!parent_0 || parent_0.nodeType != 1) && (parent_0 = null);
  return parent_0;
}

defineClass(1056, 1, {});
var impl_1;
var Lcom_google_gwt_dom_client_DOMImpl_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImpl', 1056);
function $isOrHasChild_0(parent_0, child){
  return parent_0.contains(child);
}

defineClass(1057, 1056, {});
var Lcom_google_gwt_dom_client_DOMImplStandard_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplStandard', 1057);
defineClass(1058, 1057, {});
var Lcom_google_gwt_dom_client_DOMImplStandardBase_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplStandardBase', 1058);
function DOMImplWebkit(){
}

defineClass(675, 1058, {}, DOMImplWebkit);
var Lcom_google_gwt_dom_client_DOMImplWebkit_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplWebkit', 675);
function $createDivElement(this$static){
  return ($clinit_DOMImpl() , this$static).createElement('div');
}

function $getClientHeight(this$static){
  return ($equals_6(this$static.compatMode, 'CSS1Compat')?this$static.documentElement:this$static.body).clientHeight | 0;
}

function $getClientWidth(this$static){
  return ($equals_6(this$static.compatMode, 'CSS1Compat')?this$static.documentElement:this$static.body).clientWidth | 0;
}

function $getElementById(this$static, elementId){
  return this$static.getElementById(elementId);
}

function $clinit_Style$Display(){
  $clinit_Style$Display = emptyMethod;
  NONE_0 = new Style$Display$1;
  BLOCK = new Style$Display$2;
  INLINE = new Style$Display$3;
  INLINE_BLOCK = new Style$Display$4;
  INLINE_TABLE = new Style$Display$5;
  LIST_ITEM = new Style$Display$6;
  RUN_IN = new Style$Display$7;
  TABLE = new Style$Display$8;
  TABLE_CAPTION = new Style$Display$9;
  TABLE_COLUMN_GROUP = new Style$Display$10;
  TABLE_HEADER_GROUP = new Style$Display$11;
  TABLE_FOOTER_GROUP = new Style$Display$12;
  TABLE_ROW_GROUP = new Style$Display$13;
  TABLE_CELL = new Style$Display$14;
  TABLE_COLUMN = new Style$Display$15;
  TABLE_ROW = new Style$Display$16;
  INITIAL_0 = new Style$Display$17;
  FLEX = new Style$Display$18;
  INLINE_FLEX = new Style$Display$19;
}

function Style$Display(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_33(){
  $clinit_Style$Display();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_google_gwt_dom_client_Style$Display_2_classLit, 1), $intern_3, 42, 0, [NONE_0, BLOCK, INLINE, INLINE_BLOCK, INLINE_TABLE, LIST_ITEM, RUN_IN, TABLE, TABLE_CAPTION, TABLE_COLUMN_GROUP, TABLE_HEADER_GROUP, TABLE_FOOTER_GROUP, TABLE_ROW_GROUP, TABLE_CELL, TABLE_COLUMN, TABLE_ROW, INITIAL_0, FLEX, INLINE_FLEX]);
}

defineClass(42, 8, $intern_18);
var BLOCK, FLEX, INITIAL_0, INLINE, INLINE_BLOCK, INLINE_FLEX, INLINE_TABLE, LIST_ITEM, NONE_0, RUN_IN, TABLE, TABLE_CAPTION, TABLE_CELL, TABLE_COLUMN, TABLE_COLUMN_GROUP, TABLE_FOOTER_GROUP, TABLE_HEADER_GROUP, TABLE_ROW, TABLE_ROW_GROUP;
var Lcom_google_gwt_dom_client_Style$Display_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display', 42, values_33);
function Style$Display$1(){
  Style$Display.call(this, 'NONE', 0);
}

defineClass(467, 42, $intern_18, Style$Display$1);
var Lcom_google_gwt_dom_client_Style$Display$1_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/1', 467, null);
function Style$Display$10(){
  Style$Display.call(this, 'TABLE_COLUMN_GROUP', 9);
}

defineClass(476, 42, $intern_18, Style$Display$10);
var Lcom_google_gwt_dom_client_Style$Display$10_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/10', 476, null);
function Style$Display$11(){
  Style$Display.call(this, 'TABLE_HEADER_GROUP', 10);
}

defineClass(477, 42, $intern_18, Style$Display$11);
var Lcom_google_gwt_dom_client_Style$Display$11_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/11', 477, null);
function Style$Display$12(){
  Style$Display.call(this, 'TABLE_FOOTER_GROUP', 11);
}

defineClass(478, 42, $intern_18, Style$Display$12);
var Lcom_google_gwt_dom_client_Style$Display$12_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/12', 478, null);
function Style$Display$13(){
  Style$Display.call(this, 'TABLE_ROW_GROUP', 12);
}

defineClass(479, 42, $intern_18, Style$Display$13);
var Lcom_google_gwt_dom_client_Style$Display$13_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/13', 479, null);
function Style$Display$14(){
  Style$Display.call(this, 'TABLE_CELL', 13);
}

defineClass(480, 42, $intern_18, Style$Display$14);
var Lcom_google_gwt_dom_client_Style$Display$14_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/14', 480, null);
function Style$Display$15(){
  Style$Display.call(this, 'TABLE_COLUMN', 14);
}

defineClass(481, 42, $intern_18, Style$Display$15);
var Lcom_google_gwt_dom_client_Style$Display$15_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/15', 481, null);
function Style$Display$16(){
  Style$Display.call(this, 'TABLE_ROW', 15);
}

defineClass(482, 42, $intern_18, Style$Display$16);
var Lcom_google_gwt_dom_client_Style$Display$16_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/16', 482, null);
function Style$Display$17(){
  Style$Display.call(this, 'INITIAL', 16);
}

defineClass(483, 42, $intern_18, Style$Display$17);
var Lcom_google_gwt_dom_client_Style$Display$17_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/17', 483, null);
function Style$Display$18(){
  Style$Display.call(this, 'FLEX', 17);
}

defineClass(484, 42, $intern_18, Style$Display$18);
var Lcom_google_gwt_dom_client_Style$Display$18_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/18', 484, null);
function Style$Display$19(){
  Style$Display.call(this, 'INLINE_FLEX', 18);
}

defineClass(485, 42, $intern_18, Style$Display$19);
var Lcom_google_gwt_dom_client_Style$Display$19_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/19', 485, null);
function Style$Display$2(){
  Style$Display.call(this, 'BLOCK', 1);
}

defineClass(468, 42, $intern_18, Style$Display$2);
var Lcom_google_gwt_dom_client_Style$Display$2_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/2', 468, null);
function Style$Display$3(){
  Style$Display.call(this, 'INLINE', 2);
}

defineClass(469, 42, $intern_18, Style$Display$3);
var Lcom_google_gwt_dom_client_Style$Display$3_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/3', 469, null);
function Style$Display$4(){
  Style$Display.call(this, 'INLINE_BLOCK', 3);
}

defineClass(470, 42, $intern_18, Style$Display$4);
var Lcom_google_gwt_dom_client_Style$Display$4_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/4', 470, null);
function Style$Display$5(){
  Style$Display.call(this, 'INLINE_TABLE', 4);
}

defineClass(471, 42, $intern_18, Style$Display$5);
var Lcom_google_gwt_dom_client_Style$Display$5_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/5', 471, null);
function Style$Display$6(){
  Style$Display.call(this, 'LIST_ITEM', 5);
}

defineClass(472, 42, $intern_18, Style$Display$6);
var Lcom_google_gwt_dom_client_Style$Display$6_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/6', 472, null);
function Style$Display$7(){
  Style$Display.call(this, 'RUN_IN', 6);
}

defineClass(473, 42, $intern_18, Style$Display$7);
var Lcom_google_gwt_dom_client_Style$Display$7_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/7', 473, null);
function Style$Display$8(){
  Style$Display.call(this, 'TABLE', 7);
}

defineClass(474, 42, $intern_18, Style$Display$8);
var Lcom_google_gwt_dom_client_Style$Display$8_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/8', 474, null);
function Style$Display$9(){
  Style$Display.call(this, 'TABLE_CAPTION', 8);
}

defineClass(475, 42, $intern_18, Style$Display$9);
var Lcom_google_gwt_dom_client_Style$Display$9_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Display/9', 475, null);
function $clinit_Style$Position(){
  $clinit_Style$Position = emptyMethod;
  STATIC = new Style$Position$1;
  RELATIVE = new Style$Position$2;
  ABSOLUTE = new Style$Position$3;
  FIXED = new Style$Position$4;
}

function Style$Position(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_35(){
  $clinit_Style$Position();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_google_gwt_dom_client_Style$Position_2_classLit, 1), $intern_3, 117, 0, [STATIC, RELATIVE, ABSOLUTE, FIXED]);
}

defineClass(117, 8, $intern_19);
var ABSOLUTE, FIXED, RELATIVE, STATIC;
var Lcom_google_gwt_dom_client_Style$Position_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Position', 117, values_35);
function Style$Position$1(){
  Style$Position.call(this, 'STATIC', 0);
}

defineClass(490, 117, $intern_19, Style$Position$1);
var Lcom_google_gwt_dom_client_Style$Position$1_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Position/1', 490, null);
function Style$Position$2(){
  Style$Position.call(this, 'RELATIVE', 1);
}

defineClass(491, 117, $intern_19, Style$Position$2);
var Lcom_google_gwt_dom_client_Style$Position$2_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Position/2', 491, null);
function Style$Position$3(){
  Style$Position.call(this, 'ABSOLUTE', 2);
}

defineClass(492, 117, $intern_19, Style$Position$3);
var Lcom_google_gwt_dom_client_Style$Position$3_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Position/3', 492, null);
function Style$Position$4(){
  Style$Position.call(this, 'FIXED', 3);
}

defineClass(493, 117, $intern_19, Style$Position$4);
var Lcom_google_gwt_dom_client_Style$Position$4_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Position/4', 493, null);
function $clinit_Style$Unit(){
  $clinit_Style$Unit = emptyMethod;
  PX = new Style$Unit$1;
  PCT = new Style$Unit$2;
  EM = new Style$Unit$3;
  EX = new Style$Unit$4;
  PT = new Style$Unit$5;
  PC = new Style$Unit$6;
  IN = new Style$Unit$7;
  CM = new Style$Unit$8;
  MM = new Style$Unit$9;
}

function Style$Unit(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_37(){
  $clinit_Style$Unit();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_google_gwt_dom_client_Style$Unit_2_classLit, 1), $intern_3, 68, 0, [PX, PCT, EM, EX, PT, PC, IN, CM, MM]);
}

defineClass(68, 8, $intern_20);
var CM, EM, EX, IN, MM, PC, PCT, PT, PX;
var Lcom_google_gwt_dom_client_Style$Unit_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit', 68, values_37);
function Style$Unit$1(){
  Style$Unit.call(this, 'PX', 0);
}

defineClass(440, 68, $intern_20, Style$Unit$1);
_.getType = function getType(){
  return 'px';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$1_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/1', 440, null);
function Style$Unit$2(){
  Style$Unit.call(this, 'PCT', 1);
}

defineClass(441, 68, $intern_20, Style$Unit$2);
_.getType = function getType_0(){
  return '%';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$2_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/2', 441, null);
function Style$Unit$3(){
  Style$Unit.call(this, 'EM', 2);
}

defineClass(442, 68, $intern_20, Style$Unit$3);
_.getType = function getType_1(){
  return 'em';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$3_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/3', 442, null);
function Style$Unit$4(){
  Style$Unit.call(this, 'EX', 3);
}

defineClass(443, 68, $intern_20, Style$Unit$4);
_.getType = function getType_2(){
  return 'ex';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$4_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/4', 443, null);
function Style$Unit$5(){
  Style$Unit.call(this, 'PT', 4);
}

defineClass(444, 68, $intern_20, Style$Unit$5);
_.getType = function getType_3(){
  return 'pt';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$5_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/5', 444, null);
function Style$Unit$6(){
  Style$Unit.call(this, 'PC', 5);
}

defineClass(445, 68, $intern_20, Style$Unit$6);
_.getType = function getType_4(){
  return 'pc';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$6_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/6', 445, null);
function Style$Unit$7(){
  Style$Unit.call(this, 'IN', 6);
}

defineClass(446, 68, $intern_20, Style$Unit$7);
_.getType = function getType_5(){
  return 'in';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$7_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/7', 446, null);
function Style$Unit$8(){
  Style$Unit.call(this, 'CM', 7);
}

defineClass(447, 68, $intern_20, Style$Unit$8);
_.getType = function getType_6(){
  return 'cm';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$8_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/8', 447, null);
function Style$Unit$9(){
  Style$Unit.call(this, 'MM', 8);
}

defineClass(448, 68, $intern_20, Style$Unit$9);
_.getType = function getType_7(){
  return 'mm';
}
;
var Lcom_google_gwt_dom_client_Style$Unit$9_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Unit/9', 448, null);
function $clinit_Style$Visibility(){
  $clinit_Style$Visibility = emptyMethod;
  VISIBLE_0 = new Style$Visibility$1;
  HIDDEN_1 = new Style$Visibility$2;
}

function Style$Visibility(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_38(){
  $clinit_Style$Visibility();
  return stampJavaTypeInfo(getClassLiteralForArray(Lcom_google_gwt_dom_client_Style$Visibility_2_classLit, 1), $intern_3, 182, 0, [VISIBLE_0, HIDDEN_1]);
}

defineClass(182, 8, $intern_21);
var HIDDEN_1, VISIBLE_0;
var Lcom_google_gwt_dom_client_Style$Visibility_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Visibility', 182, values_38);
function Style$Visibility$1(){
  Style$Visibility.call(this, 'VISIBLE', 0);
}

defineClass(498, 182, $intern_21, Style$Visibility$1);
var Lcom_google_gwt_dom_client_Style$Visibility$1_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Visibility/1', 498, null);
function Style$Visibility$2(){
  Style$Visibility.call(this, 'HIDDEN', 1);
}

defineClass(499, 182, $intern_21, Style$Visibility$2);
var Lcom_google_gwt_dom_client_Style$Visibility$2_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/Visibility/2', 499, null);
defineClass(412, 1, {});
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
var Lcom_google_web_bindery_event_shared_Event$Type_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event/Type', 412);
function GwtEvent$Type(){
  this.index_0 = ++nextHashCode;
}

defineClass(127, 412, {}, GwtEvent$Type);
var Lcom_google_gwt_event_shared_GwtEvent$Type_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent/Type', 127);
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

defineClass(628, 1018, {}, AttachEvent);
_.dispatch_0 = function dispatch_24(handler){
  castTo(handler, 1085).onAttachOrDetach(this);
}
;
_.getAssociatedType = function getAssociatedType_24(){
  return TYPE_23;
}
;
_.attached = false;
var TYPE_23;
var Lcom_google_gwt_event_logical_shared_AttachEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'AttachEvent', 628);
function CloseEvent_0(){
}

function fire_1(source){
  var event_0;
  if (TYPE_24) {
    event_0 = new CloseEvent_0;
    source.fireEvent(event_0);
  }
}

defineClass(627, 1018, {}, CloseEvent_0);
_.dispatch_0 = function dispatch_25(handler){
  castTo(handler, 1002).onClose(this);
}
;
_.getAssociatedType = function getAssociatedType_25(){
  return TYPE_24;
}
;
var TYPE_24;
var Lcom_google_gwt_event_logical_shared_CloseEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'CloseEvent', 627);
function ResizeEvent(){
}

function fire_2(source){
  var event_0;
  if (TYPE_25) {
    event_0 = new ResizeEvent;
    $fireEvent(source, event_0);
  }
}

defineClass(624, 1018, {}, ResizeEvent);
_.dispatch_0 = function dispatch_26(handler){
  castTo(handler, 306).onResize_0(this);
}
;
_.getAssociatedType = function getAssociatedType_26(){
  return TYPE_25;
}
;
var TYPE_25;
var Lcom_google_gwt_event_logical_shared_ResizeEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'ResizeEvent', 624);
defineClass(1020, 1, {});
var Lcom_google_web_bindery_event_shared_EventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'EventBus', 1020);
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
    if (instanceOf($e0, 136)) {
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

defineClass(176, 1, $intern_22, HandlerManager, HandlerManager_0);
_.fireEvent = function fireEvent_1(event_0){
  $fireEvent(this, event_0);
}
;
var Lcom_google_gwt_event_shared_HandlerManager_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager', 176);
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
        event_0.dispatch_0(castTo(handler, 26));
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
  sourceMap = castTo($get_0(this$static.map_0, type_0), 82);
  if (!sourceMap) {
    sourceMap = new HashMap;
    $put(this$static.map_0, type_0, sourceMap);
  }
  handlers = castTo(sourceMap.get_2(source), 41);
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
  sourceMap = castTo($get_0(this$static.map_0, type_0), 82);
  if (!sourceMap) {
    return $clinit_Collections() , $clinit_Collections() , EMPTY_LIST;
  }
  handlers = castTo(sourceMap.get_2(source), 41);
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
        c = castTo($next_3(c$iterator), 416);
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

defineClass(258, 1020, {});
_.firingDepth = 0;
_.isReverseOrder = false;
var Lcom_google_web_bindery_event_shared_SimpleEventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus', 258);
function HandlerManager$Bus(fireInReverseOrder){
  SimpleEventBus_0.call(this, fireInReverseOrder);
}

defineClass(413, 258, {}, HandlerManager$Bus);
var Lcom_google_gwt_event_shared_HandlerManager$Bus_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager/Bus', 413);
function LegacyHandlerWrapper(real){
  this.real = real;
}

defineClass(311, 1, {1001:1}, LegacyHandlerWrapper);
var Lcom_google_gwt_event_shared_LegacyHandlerWrapper_2_classLit = createForClass('com.google.gwt.event.shared', 'LegacyHandlerWrapper', 311);
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

defineClass(136, 10, {136:1, 3:1, 13:1, 10:1, 17:1}, UmbrellaException);
var Lcom_google_web_bindery_event_shared_UmbrellaException_2_classLit = createForClass('com.google.web.bindery.event.shared', 'UmbrellaException', 136);
function UmbrellaException_0(causes){
  UmbrellaException.call(this, causes);
}

defineClass(135, 136, $intern_23, UmbrellaException_0);
var Lcom_google_gwt_event_shared_UmbrellaException_2_classLit = createForClass('com.google.gwt.event.shared', 'UmbrellaException', 135);
function throwIfNull(name_0, value_0){
  if (null == value_0) {
    throw toJs(new NullPointerException_1(name_0 + ' cannot be null'));
  }
}

function $clinit_DateTimeFormat(){
  $clinit_DateTimeFormat = emptyMethod;
  new HashMap;
}

function $addPart(this$static, buf, count){
  var oldLength;
  if (buf.string.length > 0) {
    $add_13(this$static.patternParts, new DateTimeFormat$PatternPart(buf.string, count));
    oldLength = buf.string.length;
    0 < oldLength?(buf.string = buf.string.substr(0, 0)):0 > oldLength && (buf.string += valueOf_11(initUnidimensionalArray(C_classLit, $intern_17, 31, -oldLength, 15, 1)));
  }
}

function $format(this$static, date, timeZone){
  var ch_0, diff, i, j, keepDate, keepTime, n, toAppendTo, trailQuote;
  !timeZone && (timeZone = createTimeZone(date.jsdate.getTimezoneOffset()));
  diff = (date.jsdate.getTimezoneOffset() - timeZone.standardOffset) * 60000;
  keepDate = new Date_1(add_3(fromDouble_0(date.jsdate.getTime()), diff));
  keepTime = keepDate;
  if (keepDate.jsdate.getTimezoneOffset() != date.jsdate.getTimezoneOffset()) {
    diff > 0?(diff -= 86400000):(diff += 86400000);
    keepTime = new Date_1(add_3(fromDouble_0(date.jsdate.getTime()), diff));
  }
  toAppendTo = new StringBuilder_0;
  n = this$static.pattern.length;
  for (i = 0; i < n;) {
    ch_0 = $charAt(this$static.pattern, i);
    if (ch_0 >= 97 && ch_0 <= 122 || ch_0 >= 65 && ch_0 <= 90) {
      for (j = i + 1; j < n && $charAt(this$static.pattern, j) == ch_0; ++j)
      ;
      $subFormat(toAppendTo, ch_0, j - i, keepDate, keepTime, timeZone);
      i = j;
    }
     else if (ch_0 == 39) {
      ++i;
      if (i < n && $charAt(this$static.pattern, i) == 39) {
        toAppendTo.string += "'";
        ++i;
        continue;
      }
      trailQuote = false;
      while (!trailQuote) {
        j = i;
        while (j < n && $charAt(this$static.pattern, j) != 39) {
          ++j;
        }
        if (j >= n) {
          throw toJs(new IllegalArgumentException_0("Missing trailing '"));
        }
        j + 1 < n && $charAt(this$static.pattern, j + 1) == 39?++j:(trailQuote = true);
        $append_5(toAppendTo, $substring_0(this$static.pattern, i, j));
        i = j + 1;
      }
    }
     else {
      toAppendTo.string += String.fromCharCode(ch_0);
      ++i;
    }
  }
  return toAppendTo.string;
}

function $formatFractionalSeconds(buf, count, date){
  var time, value_0;
  time = fromDouble_0(date.jsdate.getTime());
  if (compare_5(time, 0) < 0) {
    value_0 = $intern_8 - toInt(mod_0(neg_0(time), $intern_8));
    value_0 == $intern_8 && (value_0 = 0);
  }
   else {
    value_0 = toInt(mod_0(time, $intern_8));
  }
  if (count == 1) {
    value_0 = $wnd.Math.min((value_0 + 50) / 100 | 0, 9);
    $append_1(buf, 48 + value_0 & $intern_24);
  }
   else if (count == 2) {
    value_0 = $wnd.Math.min((value_0 + 5) / 10 | 0, 99);
    $zeroPaddingNumber(buf, value_0, 2);
  }
   else {
    $zeroPaddingNumber(buf, value_0, 3);
    count > 3 && $zeroPaddingNumber(buf, 0, count - 3);
  }
}

function $formatMonth(buf, count, date){
  var value_0;
  value_0 = date.jsdate.getMonth();
  switch (count) {
    case 5:
      $append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['J', 'F', 'M', 'A', 'M', 'J', 'J', 'A', 'S', 'O', 'N', 'D'])[value_0]);
      break;
    case 4:
      $append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])[value_0]);
      break;
    case 3:
      $append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'])[value_0]);
      break;
    default:$zeroPaddingNumber(buf, value_0 + 1, count);
  }
}

function $formatYear(buf, count, date){
  var value_0;
  value_0 = date.jsdate.getFullYear() - 1900 + 1900;
  value_0 < 0 && (value_0 = -value_0);
  switch (count) {
    case 1:
      buf.string += value_0;
      break;
    case 2:
      $zeroPaddingNumber(buf, value_0 % 100, 2);
      break;
    default:$zeroPaddingNumber(buf, value_0, count);
  }
}

function $getNextCharCountInPattern(pattern, start_0){
  var ch_0, next;
  ch_0 = (checkCriticalStringElementIndex(start_0, pattern.length) , pattern.charCodeAt(start_0));
  next = start_0 + 1;
  while (next < pattern.length && (checkCriticalStringElementIndex(next, pattern.length) , pattern.charCodeAt(next) == ch_0)) {
    ++next;
  }
  return next - start_0;
}

function $identifyAbutStart(this$static){
  var abut, i, len;
  abut = false;
  len = this$static.patternParts.array.length;
  for (i = 0; i < len; i++) {
    if ($isNumeric(castTo($get_6(this$static.patternParts, i), 210))) {
      if (!abut && i + 1 < len && $isNumeric(castTo($get_6(this$static.patternParts, i + 1), 210))) {
        abut = true;
        castTo($get_6(this$static.patternParts, i), 210).abutStart = true;
      }
    }
     else {
      abut = false;
    }
  }
}

function $isNumeric(part){
  var i;
  if (part.count <= 0) {
    return false;
  }
  i = $indexOf_0('MLydhHmsSDkK', fromCodePoint($charAt(part.text_0, 0)));
  return i > 1 || i >= 0 && part.count < 3;
}

function $parsePattern(this$static, pattern){
  var buf, ch_0, count, i, inQuote;
  buf = new StringBuilder_0;
  inQuote = false;
  for (i = 0; i < pattern.length; i++) {
    ch_0 = (checkCriticalStringElementIndex(i, pattern.length) , pattern.charCodeAt(i));
    if (ch_0 == 32) {
      $addPart(this$static, buf, 0);
      buf.string += ' ';
      $addPart(this$static, buf, 0);
      while (i + 1 < pattern.length && (checkCriticalStringElementIndex(i + 1, pattern.length) , pattern.charCodeAt(i + 1) == 32)) {
        ++i;
      }
      continue;
    }
    if (inQuote) {
      if (ch_0 == 39) {
        if (i + 1 < pattern.length && (checkCriticalStringElementIndex(i + 1, pattern.length) , pattern.charCodeAt(i + 1) == 39)) {
          buf.string += "'";
          ++i;
        }
         else {
          inQuote = false;
        }
      }
       else {
        buf.string += String.fromCharCode(ch_0);
      }
      continue;
    }
    if ($indexOf_0('GyMLdkHmsSEcDahKzZv', fromCodePoint(ch_0)) > 0) {
      $addPart(this$static, buf, 0);
      buf.string += String.fromCharCode(ch_0);
      count = $getNextCharCountInPattern(pattern, i);
      $addPart(this$static, buf, count);
      i += count - 1;
      continue;
    }
    if (ch_0 == 39) {
      if (i + 1 < pattern.length && (checkCriticalStringElementIndex(i + 1, pattern.length) , pattern.charCodeAt(i + 1) == 39)) {
        buf.string += "'";
        ++i;
      }
       else {
        inQuote = true;
      }
    }
     else {
      buf.string += String.fromCharCode(ch_0);
    }
  }
  $addPart(this$static, buf, 0);
  $identifyAbutStart(this$static);
}

function $subFormat(buf, ch_0, count, adjustedDate, adjustedTime, timezone){
  var value_0, value0, value1, value10, value2, value3, value4, value5, value6, value7, value8, value9;
  switch (ch_0) {
    case 71:
      value0 = adjustedDate.jsdate.getFullYear() - 1900 >= -1900?1:0;
      count >= 4?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Before Christ', 'Anno Domini'])[value0]):$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['BC', 'AD'])[value0]);
      break;
    case 121:
      $formatYear(buf, count, adjustedDate);
      break;
    case 77:
      $formatMonth(buf, count, adjustedDate);
      break;
    case 107:
      value1 = adjustedTime.jsdate.getHours();
      value1 == 0?$zeroPaddingNumber(buf, 24, count):$zeroPaddingNumber(buf, value1, count);
      break;
    case 83:
      $formatFractionalSeconds(buf, count, adjustedTime);
      break;
    case 69:
      value2 = adjustedDate.jsdate.getDay();
      count == 5?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['S', 'M', 'T', 'W', 'T', 'F', 'S'])[value2]):count == 4?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'])[value2]):$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'])[value2]);
      break;
    case 97:
      adjustedTime.jsdate.getHours() >= 12 && adjustedTime.jsdate.getHours() < 24?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['AM', 'PM'])[1]):$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['AM', 'PM'])[0]);
      break;
    case 104:
      value3 = adjustedTime.jsdate.getHours() % 12;
      value3 == 0?$zeroPaddingNumber(buf, 12, count):$zeroPaddingNumber(buf, value3, count);
      break;
    case 75:
      value4 = adjustedTime.jsdate.getHours() % 12;
      $zeroPaddingNumber(buf, value4, count);
      break;
    case 72:
      value5 = adjustedTime.jsdate.getHours();
      $zeroPaddingNumber(buf, value5, count);
      break;
    case 99:
      value6 = adjustedDate.jsdate.getDay();
      count == 5?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['S', 'M', 'T', 'W', 'T', 'F', 'S'])[value6]):count == 4?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'])[value6]):count == 3?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'])[value6]):$zeroPaddingNumber(buf, value6, 1);
      break;
    case 76:
      value7 = adjustedDate.jsdate.getMonth();
      count == 5?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['J', 'F', 'M', 'A', 'M', 'J', 'J', 'A', 'S', 'O', 'N', 'D'])[value7]):count == 4?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])[value7]):count == 3?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'])[value7]):$zeroPaddingNumber(buf, value7 + 1, count);
      break;
    case 81:
      value8 = adjustedDate.jsdate.getMonth() / 3 | 0;
      count < 4?$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['Q1', 'Q2', 'Q3', 'Q4'])[value8]):$append_5(buf, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_6, 2, 6, ['1st quarter', '2nd quarter', '3rd quarter', '4th quarter'])[value8]);
      break;
    case 100:
      value9 = adjustedDate.jsdate.getDate();
      $zeroPaddingNumber(buf, value9, count);
      break;
    case 109:
      value10 = adjustedTime.jsdate.getMinutes();
      $zeroPaddingNumber(buf, value10, count);
      break;
    case 115:
      value_0 = adjustedTime.jsdate.getSeconds();
      $zeroPaddingNumber(buf, value_0, count);
      break;
    case 122:
      count < 4?$append_5(buf, timezone.tzNames[0]):$append_5(buf, timezone.tzNames[1]);
      break;
    case 118:
      $append_5(buf, timezone.timezoneID);
      break;
    case 90:
      count < 3?$append_5(buf, $getRFCTimeZoneString(timezone)):count == 3?$append_5(buf, $getISOTimeZoneString(timezone)):$append_5(buf, composeGMTString(timezone.standardOffset));
      break;
    default:return false;
  }
  return true;
}

function $zeroPaddingNumber(buf, value_0, minWidth){
  var b, i;
  b = 10;
  for (i = 0; i < minWidth - 1; i++) {
    value_0 < b && (buf.string += '0' , buf);
    b *= 10;
  }
  buf.string += value_0;
}

defineClass(622, 1, {});
var Lcom_google_gwt_i18n_shared_DateTimeFormat_2_classLit = createForClass('com.google.gwt.i18n.shared', 'DateTimeFormat', 622);
function $clinit_DateTimeFormat_0(){
  $clinit_DateTimeFormat_0 = emptyMethod;
  $clinit_DateTimeFormat();
  cache_0 = new HashMap;
}

function DateTimeFormat(pattern){
  $clinit_DateTimeFormat();
  this.patternParts = new ArrayList;
  this.pattern = pattern;
  $parsePattern(this, pattern);
}

function getFormat(pattern, dtfi){
  $clinit_DateTimeFormat_0();
  var defaultDtfi, dtf;
  defaultDtfi = $getDateTimeFormatInfo(($clinit_LocaleInfo() , $clinit_LocaleInfo() , instance_5));
  dtf = null;
  dtfi == defaultDtfi && (dtf = castTo($getStringValue(cache_0, pattern), 274));
  if (!dtf) {
    dtf = new DateTimeFormat(pattern);
    dtfi == defaultDtfi && $putStringValue(cache_0, pattern, dtf);
  }
  return dtf;
}

defineClass(274, 622, {274:1}, DateTimeFormat);
var cache_0;
var Lcom_google_gwt_i18n_client_DateTimeFormat_2_classLit = createForClass('com.google.gwt.i18n.client', 'DateTimeFormat', 274);
defineClass(1067, 1, {});
var Lcom_google_gwt_i18n_shared_DefaultDateTimeFormatInfo_2_classLit = createForClass('com.google.gwt.i18n.shared', 'DefaultDateTimeFormatInfo', 1067);
defineClass(1068, 1067, {});
var Lcom_google_gwt_i18n_client_DefaultDateTimeFormatInfo_2_classLit = createForClass('com.google.gwt.i18n.client', 'DefaultDateTimeFormatInfo', 1068);
function $clinit_LocaleInfo(){
  $clinit_LocaleInfo = emptyMethod;
  instance_5 = new LocaleInfo;
}

function $getDateTimeFormatInfo(this$static){
  !this$static.dateTimeFormatInfo && (this$static.dateTimeFormatInfo = new DateTimeFormatInfoImpl);
  return this$static.dateTimeFormatInfo;
}

function LocaleInfo(){
}

defineClass(523, 1, {}, LocaleInfo);
var instance_5;
var Lcom_google_gwt_i18n_client_LocaleInfo_2_classLit = createForClass('com.google.gwt.i18n.client', 'LocaleInfo', 523);
function $getISOTimeZoneString(this$static){
  var data_0, offset;
  offset = -this$static.standardOffset;
  data_0 = stampJavaTypeInfo(getClassLiteralForArray(C_classLit, 1), $intern_17, 31, 15, [43, 48, 48, 58, 48, 48]);
  if (offset < 0) {
    data_0[0] = 45;
    offset = -offset;
  }
  data_0[1] = data_0[1] + ((offset / 60 | 0) / 10 | 0) & $intern_24;
  data_0[2] = data_0[2] + (offset / 60 | 0) % 10 & $intern_24;
  data_0[4] = data_0[4] + (offset % 60 / 10 | 0) & $intern_24;
  data_0[5] = data_0[5] + offset % 10 & $intern_24;
  return valueOf_12(data_0, 0, data_0.length);
}

function $getRFCTimeZoneString(this$static){
  var data_0, offset;
  offset = -this$static.standardOffset;
  data_0 = stampJavaTypeInfo(getClassLiteralForArray(C_classLit, 1), $intern_17, 31, 15, [43, 48, 48, 48, 48]);
  if (offset < 0) {
    data_0[0] = 45;
    offset = -offset;
  }
  data_0[1] = data_0[1] + ((offset / 60 | 0) / 10 | 0) & $intern_24;
  data_0[2] = data_0[2] + (offset / 60 | 0) % 10 & $intern_24;
  data_0[3] = data_0[3] + (offset % 60 / 10 | 0) & $intern_24;
  data_0[4] = data_0[4] + offset % 10 & $intern_24;
  return valueOf_12(data_0, 0, data_0.length);
}

function TimeZone(){
}

function composeGMTString(offset){
  var data_0;
  data_0 = stampJavaTypeInfo(getClassLiteralForArray(C_classLit, 1), $intern_17, 31, 15, [71, 77, 84, 45, 48, 48, 58, 48, 48]);
  if (offset <= 0) {
    data_0[3] = 43;
    offset = -offset;
  }
  data_0[4] = data_0[4] + ((offset / 60 | 0) / 10 | 0) & $intern_24;
  data_0[5] = data_0[5] + (offset / 60 | 0) % 10 & $intern_24;
  data_0[7] = data_0[7] + (offset % 60 / 10 | 0) & $intern_24;
  data_0[8] = data_0[8] + offset % 10 & $intern_24;
  return valueOf_12(data_0, 0, data_0.length);
}

function composePOSIXTimeZoneID(offset){
  var str;
  if (offset == 0) {
    return 'Etc/GMT';
  }
  if (offset < 0) {
    offset = -offset;
    str = 'Etc/GMT-';
  }
   else {
    str = 'Etc/GMT+';
  }
  return str + offsetDisplay(offset);
}

function composeUTCString(offset){
  var str;
  if (offset == 0) {
    return 'UTC';
  }
  if (offset < 0) {
    offset = -offset;
    str = 'UTC+';
  }
   else {
    str = 'UTC-';
  }
  return str + offsetDisplay(offset);
}

function createTimeZone(timeZoneOffsetInMinutes){
  var tz;
  tz = new TimeZone;
  tz.standardOffset = timeZoneOffsetInMinutes;
  tz.timezoneID = composePOSIXTimeZoneID(timeZoneOffsetInMinutes);
  tz.tzNames = initUnidimensionalArray(Ljava_lang_String_2_classLit, $intern_6, 2, 2, 6, 1);
  tz.tzNames[0] = composeUTCString(timeZoneOffsetInMinutes);
  tz.tzNames[1] = composeUTCString(timeZoneOffsetInMinutes);
  return tz;
}

function offsetDisplay(offset){
  var hour, mins;
  hour = offset / 60 | 0;
  mins = offset % 60;
  if (mins == 0) {
    return '' + hour;
  }
  return '' + hour + ':' + ('' + mins);
}

defineClass(754, 1, {}, TimeZone);
_.standardOffset = 0;
var Lcom_google_gwt_i18n_client_TimeZone_2_classLit = createForClass('com.google.gwt.i18n.client', 'TimeZone', 754);
function DateTimeFormatInfoImpl(){
}

defineClass(753, 1068, {}, DateTimeFormatInfoImpl);
var Lcom_google_gwt_i18n_client_impl_cldr_DateTimeFormatInfoImpl_2_classLit = createForClass('com.google.gwt.i18n.client.impl.cldr', 'DateTimeFormatInfoImpl', 753);
function DateTimeFormat$PatternPart(txt, cnt){
  this.text_0 = txt;
  this.count = cnt;
  this.abutStart = false;
}

defineClass(210, 1, {210:1}, DateTimeFormat$PatternPart);
_.abutStart = false;
_.count = 0;
var Lcom_google_gwt_i18n_shared_DateTimeFormat$PatternPart_2_classLit = createForClass('com.google.gwt.i18n.shared', 'DateTimeFormat/PatternPart', 210);
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
  a0 = value_0 & $intern_25;
  a1 = value_0 >> 22 & $intern_25;
  a2 = value_0 < 0?$intern_26:0;
  return create0(a0, a1, a2);
}

function create_3(a){
  return create0(a.l, a.m, a.h);
}

function create0(l, m, h){
  return {l:l, m:m, h:h};
}

function divMod(a, b, computeRemainder){
  var aIsCopy, aIsMinValue, aIsNegative, bpower, c, negative;
  if (b.l == 0 && b.m == 0 && b.h == 0) {
    throw toJs(new ArithmeticException);
  }
  if (a.l == 0 && a.m == 0 && a.h == 0) {
    computeRemainder && (remainder = create0(0, 0, 0));
    return create0(0, 0, 0);
  }
  if (b.h == $intern_27 && b.m == 0 && b.l == 0) {
    return divModByMinValue(a, computeRemainder);
  }
  negative = false;
  if (b.h >> 19 != 0) {
    b = neg(b);
    negative = true;
  }
  bpower = powerOfTwo(b);
  aIsNegative = false;
  aIsMinValue = false;
  aIsCopy = false;
  if (a.h == $intern_27 && a.m == 0 && a.l == 0) {
    aIsMinValue = true;
    aIsNegative = true;
    if (bpower == -1) {
      a = create_3(($clinit_BigLongLib$Const() , MAX_VALUE));
      aIsCopy = true;
      negative = !negative;
    }
     else {
      c = shr(a, bpower);
      negative && negate(c);
      computeRemainder && (remainder = create0(0, 0, 0));
      return c;
    }
  }
   else if (a.h >> 19 != 0) {
    aIsNegative = true;
    a = neg(a);
    aIsCopy = true;
    negative = !negative;
  }
  if (bpower != -1) {
    return divModByShift(a, bpower, negative, aIsNegative, computeRemainder);
  }
  if (compare_4(a, b) < 0) {
    computeRemainder && (aIsNegative?(remainder = neg(a)):(remainder = create0(a.l, a.m, a.h)));
    return create0(0, 0, 0);
  }
  return divModHelper(aIsCopy?a:create0(a.l, a.m, a.h), b, negative, aIsNegative, aIsMinValue, computeRemainder);
}

function divModByMinValue(a, computeRemainder){
  if (a.h == $intern_27 && a.m == 0 && a.l == 0) {
    computeRemainder && (remainder = create0(0, 0, 0));
    return create_3(($clinit_BigLongLib$Const() , ONE));
  }
  computeRemainder && (remainder = create0(a.l, a.m, a.h));
  return create0(0, 0, 0);
}

function divModByShift(a, bpower, negative, aIsNegative, computeRemainder){
  var c;
  c = shr(a, bpower);
  negative && negate(c);
  if (computeRemainder) {
    a = maskRight(a, bpower);
    aIsNegative?(remainder = neg(a)):(remainder = create0(a.l, a.m, a.h));
  }
  return c;
}

function divModHelper(a, b, negative, aIsNegative, aIsMinValue, computeRemainder){
  var bshift, gte, quotient, shift_0, a1, a2, a0;
  shift_0 = numberOfLeadingZeros(b) - numberOfLeadingZeros(a);
  bshift = shl(b, shift_0);
  quotient = create0(0, 0, 0);
  while (shift_0 >= 0) {
    gte = trialSubtract(a, bshift);
    if (gte) {
      shift_0 < 22?(quotient.l |= 1 << shift_0 , undefined):shift_0 < 44?(quotient.m |= 1 << shift_0 - 22 , undefined):(quotient.h |= 1 << shift_0 - 44 , undefined);
      if (a.l == 0 && a.m == 0 && a.h == 0) {
        break;
      }
    }
    a1 = bshift.m;
    a2 = bshift.h;
    a0 = bshift.l;
    bshift.h = a2 >>> 1;
    bshift.m = a1 >>> 1 | (a2 & 1) << 21;
    bshift.l = a0 >>> 1 | (a1 & 1) << 21;
    --shift_0;
  }
  negative && negate(quotient);
  if (computeRemainder) {
    if (aIsNegative) {
      remainder = neg(a);
      aIsMinValue && (remainder = sub_0(remainder, ($clinit_BigLongLib$Const() , ONE)));
    }
     else {
      remainder = create0(a.l, a.m, a.h);
    }
  }
  return quotient;
}

function maskRight(a, bits){
  var b0, b1, b2;
  if (bits <= 22) {
    b0 = a.l & (1 << bits) - 1;
    b1 = b2 = 0;
  }
   else if (bits <= 44) {
    b0 = a.l;
    b1 = a.m & (1 << bits - 22) - 1;
    b2 = 0;
  }
   else {
    b0 = a.l;
    b1 = a.m;
    b2 = a.h & (1 << bits - 44) - 1;
  }
  return create0(b0, b1, b2);
}

function negate(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_25;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_25;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_26;
  a.l = neg0;
  a.m = neg1;
  a.h = neg2;
}

function numberOfLeadingZeros(a){
  var b1, b2;
  b2 = numberOfLeadingZeros_0(a.h);
  if (b2 == 32) {
    b1 = numberOfLeadingZeros_0(a.m);
    return b1 == 32?numberOfLeadingZeros_0(a.l) + 32:b1 + 20 - 10;
  }
   else {
    return b2 - 12;
  }
}

function powerOfTwo(a){
  var h, l, m;
  l = a.l;
  if ((l & l - 1) != 0) {
    return -1;
  }
  m = a.m;
  if ((m & m - 1) != 0) {
    return -1;
  }
  h = a.h;
  if ((h & h - 1) != 0) {
    return -1;
  }
  if (h == 0 && m == 0 && l == 0) {
    return -1;
  }
  if (h == 0 && m == 0 && l != 0) {
    return numberOfTrailingZeros(l);
  }
  if (h == 0 && m != 0 && l == 0) {
    return numberOfTrailingZeros(m) + 22;
  }
  if (h != 0 && m == 0 && l == 0) {
    return numberOfTrailingZeros(h) + 44;
  }
  return -1;
}

function toDoubleHelper(a){
  return a.l + a.m * $intern_28 + a.h * $intern_29;
}

function trialSubtract(a, b){
  var sum0, sum1, sum2;
  sum2 = a.h - b.h;
  if (sum2 < 0) {
    return false;
  }
  sum0 = a.l - b.l;
  sum1 = a.m - b.m + (sum0 >> 22);
  sum2 += sum1 >> 22;
  if (sum2 < 0) {
    return false;
  }
  a.l = sum0 & $intern_25;
  a.m = sum1 & $intern_25;
  a.h = sum2 & $intern_26;
  return true;
}

var remainder;
function add_2(a, b){
  var sum0, sum1, sum2;
  sum0 = a.l + b.l;
  sum1 = a.m + b.m + (sum0 >> 22);
  sum2 = a.h + b.h + (sum1 >> 22);
  return create0(sum0 & $intern_25, sum1 & $intern_25, sum2 & $intern_26);
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
  if (value_0 >= $intern_29) {
    a2 = round_int(value_0 / $intern_29);
    value_0 -= a2 * $intern_29;
  }
  a1 = 0;
  if (value_0 >= $intern_28) {
    a1 = round_int(value_0 / $intern_28);
    value_0 -= a1 * $intern_28;
  }
  a0 = round_int(value_0);
  result = create0(a0, a1, a2);
  negative && negate(result);
  return result;
}

function neg(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_25;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_25;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_26;
  return create0(neg0, neg1, neg2);
}

function shl(a, n){
  var res0, res1, res2;
  n &= 63;
  if (n < 22) {
    res0 = a.l << n;
    res1 = a.m << n | a.l >> 22 - n;
    res2 = a.h << n | a.m >> 22 - n;
  }
   else if (n < 44) {
    res0 = 0;
    res1 = a.l << n - 22;
    res2 = a.m << n - 22 | a.l >> 44 - n;
  }
   else {
    res0 = 0;
    res1 = 0;
    res2 = a.l << n - 44;
  }
  return create0(res0 & $intern_25, res1 & $intern_25, res2 & $intern_26);
}

function shr(a, n){
  var a2, negative, res0, res1, res2;
  n &= 63;
  a2 = a.h;
  negative = (a2 & $intern_27) != 0;
  negative && (a2 |= -1048576);
  if (n < 22) {
    res2 = a2 >> n;
    res1 = a.m >> n | a2 << 22 - n;
    res0 = a.l >> n | a.m << 22 - n;
  }
   else if (n < 44) {
    res2 = negative?$intern_26:0;
    res1 = a2 >> n - 22;
    res0 = a.m >> n - 22 | a2 << 44 - n;
  }
   else {
    res2 = negative?$intern_26:0;
    res1 = negative?$intern_25:0;
    res0 = a2 >> n - 44;
  }
  return create0(res0 & $intern_25, res1 & $intern_25, res2 & $intern_26);
}

function shru(a, n){
  var a2, res0, res1, res2;
  n &= 63;
  a2 = a.h & $intern_26;
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
  return create0(res0 & $intern_25, res1 & $intern_25, res2 & $intern_26);
}

function sub_0(a, b){
  var sum0, sum1, sum2;
  sum0 = a.l - b.l;
  sum1 = a.m - b.m + (sum0 >> 22);
  sum2 = a.h - b.h + (sum1 >> 22);
  return create0(sum0 & $intern_25, sum1 & $intern_25, sum2 & $intern_26);
}

function toDouble(a){
  if (compare_4(a, ($clinit_BigLongLib$Const() , ZERO)) < 0) {
    return -toDoubleHelper(neg(a));
  }
  return a.l + a.m * $intern_28 + a.h * $intern_29;
}

function xor(a, b){
  return create0(a.l ^ b.l, a.m ^ b.m, a.h ^ b.h);
}

function $clinit_BigLongLib$Const(){
  $clinit_BigLongLib$Const = emptyMethod;
  MAX_VALUE = create0($intern_25, $intern_25, 524287);
  MIN_VALUE = create0(0, 0, $intern_27);
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

function add_3(a, b){
  var result;
  if (isSmallLong0(a) && isSmallLong0(b)) {
    result = a + b;
    if ($intern_30 < result && result < $intern_29) {
      return result;
    }
  }
  return createLongEmul(add_2(isSmallLong0(a)?toBigLong(a):a, isSmallLong0(b)?toBigLong(b):b));
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
    return big_0.l + big_0.m * $intern_28;
  }
  if (a2 == $intern_26) {
    return big_0.l + big_0.m * $intern_28 - $intern_29;
  }
  return big_0;
}

function eq(a, b){
  return compare_5(a, b) == 0;
}

function fromDouble_0(value_0){
  if ($intern_30 < value_0 && value_0 < $intern_29) {
    return value_0 < 0?$wnd.Math.ceil(value_0):$wnd.Math.floor(value_0);
  }
  return createLongEmul(fromDouble(value_0));
}

function isSmallLong0(value_0){
  return typeof value_0 === 'number';
}

function mod_0(a, b){
  var result;
  if (isSmallLong0(a) && isSmallLong0(b)) {
    result = a % b;
    if ($intern_30 < result && result < $intern_29) {
      return result;
    }
  }
  return createLongEmul((divMod(isSmallLong0(a)?toBigLong(a):a, isSmallLong0(b)?toBigLong(b):b, true) , remainder));
}

function neg_0(a){
  var result;
  if (isSmallLong0(a)) {
    result = 0 - a;
    if (!isNaN(result)) {
      return result;
    }
  }
  return createLongEmul(neg(a));
}

function shru_0(a, n){
  return createLongEmul(shru(isSmallLong0(a)?toBigLong(a):a, n));
}

function toBigLong(longValue){
  var a0, a1, a3, value_0;
  value_0 = longValue;
  a3 = 0;
  if (value_0 < 0) {
    value_0 += $intern_29;
    a3 = $intern_26;
  }
  a1 = round_int(value_0 / $intern_28);
  a0 = round_int(value_0 - a1 * $intern_28);
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

function init_2(){
  $wnd.setTimeout($entry(assertCompileTimeUserAgent));
  $onModuleLoad_1();
  $clinit_LogConfiguration();
  $onModuleLoad_0();
  $onModuleLoad(new BaseletGWT);
}

function $adjustHorizontalConstraints(this$static, parentWidth, l){
  var leftPx, rightPx, widthPx;
  leftPx = l.left_0 * $getUnitSize(this$static, l.leftUnit, false);
  rightPx = l.right * $getUnitSize(this$static, l.rightUnit, false);
  widthPx = l.width_0 * $getUnitSize(this$static, l.widthUnit, false);
  if (l.setLeft && !l.setTargetLeft) {
    l.setLeft = false;
    if (l.setWidth) {
      l.setTargetRight = true;
      l.sourceRight = (parentWidth - (leftPx + widthPx)) / $getUnitSize(this$static, l.targetRightUnit, false);
    }
     else {
      l.setTargetWidth = true;
      l.sourceWidth = (parentWidth - (leftPx + rightPx)) / $getUnitSize(this$static, l.targetWidthUnit, false);
    }
  }
   else if (l.setWidth && !l.setTargetWidth) {
    l.setWidth = false;
    if (l.setLeft) {
      l.setTargetRight = true;
      l.sourceRight = (parentWidth - (leftPx + widthPx)) / $getUnitSize(this$static, l.targetRightUnit, false);
    }
     else {
      l.setTargetLeft = true;
      l.sourceLeft = (parentWidth - (rightPx + widthPx)) / $getUnitSize(this$static, l.targetLeftUnit, false);
    }
  }
   else if (l.setRight && !l.setTargetRight) {
    l.setRight = false;
    if (l.setWidth) {
      l.setTargetLeft = true;
      l.sourceLeft = (parentWidth - (rightPx + widthPx)) / $getUnitSize(this$static, l.targetLeftUnit, false);
    }
     else {
      l.setTargetWidth = true;
      l.sourceWidth = (parentWidth - (leftPx + rightPx)) / $getUnitSize(this$static, l.targetWidthUnit, false);
    }
  }
  l.setLeft = l.setTargetLeft;
  l.setRight = l.setTargetRight;
  l.setWidth = l.setTargetWidth;
  l.leftUnit = l.targetLeftUnit;
  l.rightUnit = l.targetRightUnit;
  l.widthUnit = l.targetWidthUnit;
}

function $adjustVerticalConstraints(this$static, parentHeight, l){
  var bottomPx, heightPx, topPx;
  topPx = l.top_0 * $getUnitSize(this$static, l.topUnit, true);
  bottomPx = l.bottom * $getUnitSize(this$static, l.bottomUnit, true);
  heightPx = l.height_0 * $getUnitSize(this$static, l.heightUnit, true);
  if (l.setTop && !l.setTargetTop) {
    l.setTop = false;
    if (l.setHeight) {
      l.setTargetBottom = true;
      l.sourceBottom = (parentHeight - (topPx + heightPx)) / $getUnitSize(this$static, l.targetBottomUnit, true);
    }
     else {
      l.setTargetHeight = true;
      l.sourceHeight = (parentHeight - (topPx + bottomPx)) / $getUnitSize(this$static, l.targetHeightUnit, true);
    }
  }
   else if (l.setHeight && !l.setTargetHeight) {
    l.setHeight = false;
    if (l.setTop) {
      l.setTargetBottom = true;
      l.sourceBottom = (parentHeight - (topPx + heightPx)) / $getUnitSize(this$static, l.targetBottomUnit, true);
    }
     else {
      l.setTargetTop = true;
      l.sourceTop = (parentHeight - (bottomPx + heightPx)) / $getUnitSize(this$static, l.targetTopUnit, true);
    }
  }
   else if (l.setBottom && !l.setTargetBottom) {
    l.setBottom = false;
    if (l.setHeight) {
      l.setTargetTop = true;
      l.sourceTop = (parentHeight - (bottomPx + heightPx)) / $getUnitSize(this$static, l.targetTopUnit, true);
    }
     else {
      l.setTargetHeight = true;
      l.sourceHeight = (parentHeight - (topPx + bottomPx)) / $getUnitSize(this$static, l.targetHeightUnit, true);
    }
  }
  l.setTop = l.setTargetTop;
  l.setBottom = l.setTargetBottom;
  l.setHeight = l.setTargetHeight;
  l.topUnit = l.targetTopUnit;
  l.bottomUnit = l.targetBottomUnit;
  l.heightUnit = l.targetHeightUnit;
}

function $getUnitSize(this$static, unit, vertical){
  return $getUnitSizeInPixels(this$static.impl, this$static.parentElem, unit, vertical);
}

function $layout(this$static, duration, callback){
  var child, l, l$iterator, l$iterator0, parentHeight, parentWidth;
  !!this$static.animation && $cancel_0(this$static.animation);
  if (duration == 0) {
    for (l$iterator0 = new ArrayList$1(this$static.layers); l$iterator0.i < l$iterator0.this$01.array.length;) {
      l = castTo($next_3(l$iterator0), 185);
      l.left_0 = l.sourceLeft = l.targetLeft;
      l.top_0 = l.sourceTop = l.targetTop;
      l.right = l.sourceRight = l.targetRight;
      l.bottom = l.sourceBottom = l.targetBottom;
      l.width_0 = l.sourceWidth = l.targetWidth;
      l.height_0 = l.sourceHeight = l.targetHeight;
      l.setLeft = l.setTargetLeft;
      l.setTop = l.setTargetTop;
      l.setRight = l.setTargetRight;
      l.setBottom = l.setTargetBottom;
      l.setWidth = l.setTargetWidth;
      l.setHeight = l.setTargetHeight;
      l.leftUnit = l.targetLeftUnit;
      l.topUnit = l.targetTopUnit;
      l.rightUnit = l.targetRightUnit;
      l.bottomUnit = l.targetBottomUnit;
      l.widthUnit = l.targetWidthUnit;
      l.heightUnit = l.targetHeightUnit;
      $layout_0(l);
      !!callback && (child = l.getUserObject() , instanceOf(child, 64) && castTo(child, 64).onResize());
    }
    return;
  }
  parentWidth = this$static.parentElem.clientWidth | 0;
  parentHeight = this$static.parentElem.clientHeight | 0;
  for (l$iterator = new ArrayList$1(this$static.layers); l$iterator.i < l$iterator.this$01.array.length;) {
    l = castTo($next_3(l$iterator), 185);
    $adjustHorizontalConstraints(this$static, parentWidth, l);
    $adjustVerticalConstraints(this$static, parentHeight, l);
  }
  this$static.animation = new Layout$1(this$static, callback);
  $run_0(this$static.animation, duration, this$static.parentElem);
}

function $removeChild_0(this$static, layer){
  $removeChild_1(layer.container, layer.child);
  $remove_12(this$static.layers, layer);
}

function Layout(parent_0){
  this.impl = new LayoutImpl;
  this.layers = new ArrayList;
  this.parentElem = parent_0;
  $initParent(this.impl, parent_0);
}

defineClass(262, 1, {}, Layout);
var Lcom_google_gwt_layout_client_Layout_2_classLit = createForClass('com.google.gwt.layout.client', 'Layout', 262);
function Layout$1(this$0, val$callback){
  this.this$01 = this$0;
  this.val$callback2 = val$callback;
  Animation.call(this);
}

defineClass(538, 263, {}, Layout$1);
_.onCancel = function onCancel_0(){
  this.this$01.animation = null;
  $layout(this.this$01, 0, null);
}
;
_.onComplete = function onComplete_0(){
  this.this$01.animation = null;
  $layout(this.this$01, 0, null);
}
;
_.onUpdate = function onUpdate(progress){
  var child, l, l$iterator;
  for (l$iterator = new ArrayList$1(this.this$01.layers); l$iterator.i < l$iterator.this$01.array.length;) {
    l = castTo($next_3(l$iterator), 185);
    l.setTargetLeft && (l.left_0 = l.sourceLeft + (l.targetLeft - l.sourceLeft) * progress);
    l.setTargetRight && (l.right = l.sourceRight + (l.targetRight - l.sourceRight) * progress);
    l.setTargetTop && (l.top_0 = l.sourceTop + (l.targetTop - l.sourceTop) * progress);
    l.setTargetBottom && (l.bottom = l.sourceBottom + (l.targetBottom - l.sourceBottom) * progress);
    l.setTargetWidth && (l.width_0 = l.sourceWidth + (l.targetWidth - l.sourceWidth) * progress);
    l.setTargetHeight && (l.height_0 = l.sourceHeight + (l.targetHeight - l.sourceHeight) * progress);
    $layout_0(l);
    !!this.val$callback2 && (child = l.getUserObject() , instanceOf(child, 64) && castTo(child, 64).onResize());
  }
}
;
var Lcom_google_gwt_layout_client_Layout$1_2_classLit = createForClass('com.google.gwt.layout.client', 'Layout/1', 538);
function $clinit_LayoutImpl(){
  $clinit_LayoutImpl = emptyMethod;
  fixedRuler = createRuler(($clinit_Style$Unit() , CM), CM);
  $appendChild($doc.body, fixedRuler);
}

function $fillParent(elem){
  var style;
  style = elem.style;
  style['position'] = ($clinit_Style$Position() , 'absolute');
  style['left'] = ($clinit_Style$Unit() , '0.0px');
  style['top'] = '0.0px';
  style['right'] = '0.0px';
  style['bottom'] = '0.0px';
}

function $getUnitSizeInPixels(this$static, parent_0, unit, vertical){
  if (!unit) {
    return 1;
  }
  switch (unit.ordinal) {
    case 1:
      return (vertical?parent_0.clientHeight | 0:parent_0.clientWidth | 0) / 100;
    case 2:
      return ((this$static.relativeRuler.offsetWidth || 0) | 0) / 10;
    case 3:
      return ((this$static.relativeRuler.offsetHeight || 0) | 0) / 10;
    case 7:
      return ((fixedRuler.offsetWidth || 0) | 0) * 0.1;
    case 8:
      return ((fixedRuler.offsetWidth || 0) | 0) * 0.01;
    case 6:
      return ((fixedRuler.offsetWidth || 0) | 0) * 0.254;
    case 4:
      return ((fixedRuler.offsetWidth || 0) | 0) * 0.00353;
    case 5:
      return ((fixedRuler.offsetWidth || 0) | 0) * 0.0423;
    default:case 0:
      return 1;
  }
}

function $initParent(this$static, parent_0){
  parent_0.style['position'] = ($clinit_Style$Position() , 'relative');
  $appendChild(parent_0, this$static.relativeRuler = createRuler(($clinit_Style$Unit() , EM), EX));
}

function $layout_0(layer){
  var style;
  style = layer.container.style;
  layer.visible?(style['display'] = '' , undefined):(style['display'] = ($clinit_Style$Display() , 'none') , undefined);
  style['left'] = layer.setLeft?layer.left_0 + layer.leftUnit.getType():'';
  style['top'] = layer.setTop?layer.top_0 + layer.topUnit.getType():'';
  style['right'] = layer.setRight?layer.right + layer.rightUnit.getType():'';
  style['bottom'] = layer.setBottom?layer.bottom + layer.bottomUnit.getType():'';
  style['width'] = layer.setWidth?layer.width_0 + layer.widthUnit.getType():'';
  style['height'] = layer.setHeight?layer.height_0 + layer.heightUnit.getType():'';
  style = layer.child.style;
  switch (2) {
    case 2:
      style['left'] = ($clinit_Style$Unit() , '0.0px');
      style['right'] = '0.0px';
  }
  switch (2) {
    case 2:
      style['top'] = ($clinit_Style$Unit() , '0.0px');
      style['bottom'] = '0.0px';
  }
}

function $removeChild_1(container, child){
  var parent_0, parent0, style;
  parent0 = $getParentElement_0(($clinit_DOMImpl() , container));
  !!parent0 && parent0.removeChild(container);
  $getParentElement_0(child) == container && (parent_0 = $getParentElement_0(child) , !!parent_0 && parent_0.removeChild(child));
  style = child.style;
  style['position'] = '';
  style['left'] = '';
  style['top'] = '';
  style['width'] = '';
  style['height'] = '';
}

function LayoutImpl(){
  $clinit_LayoutImpl();
}

function createRuler(widthUnit, heightUnit){
  var ruler, style;
  ruler = $createDivElement($doc);
  ruler.innerHTML = '&nbsp;';
  style = ruler.style;
  style['position'] = ($clinit_Style$Position() , 'absolute');
  style['zIndex'] = '-32767';
  style['top'] = -20 + heightUnit.getType();
  style['width'] = 10 + widthUnit.getType();
  style['height'] = 10 + heightUnit.getType();
  style['visibility'] = ($clinit_Style$Visibility() , 'hidden');
  $set_1(($clinit_State() , HIDDEN), ruler, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Boolean_2_classLit, 1), $intern_3, 315, 8, [($clinit_Boolean() , true)]));
  return ruler;
}

defineClass(657, 1, {}, LayoutImpl);
var fixedRuler;
var Lcom_google_gwt_layout_client_LayoutImpl_2_classLit = createForClass('com.google.gwt.layout.client', 'LayoutImpl', 657);
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

defineClass(179, 1, $intern_31);
var Ljava_util_logging_Handler_2_classLit = createForClass('java.util.logging', 'Handler', 179);
function ConsoleLogHandler(){
  $setFormatter(this, new TextLogFormatter(true));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(402, 179, $intern_31, ConsoleLogHandler);
_.publish = function publish(record){
  var msg, val;
  if (!window.console || ($getLevel(this) , $intern_7 > record.level.intValue())) {
    return;
  }
  msg = $format_0(this.formatter, record);
  val = record.level.intValue();
  val >= ($clinit_Level() , $intern_8)?(window.console.error(msg) , undefined):val >= 900?(window.console.warn(msg) , undefined):val >= 800?(window.console.info(msg) , undefined):(window.console.log(msg) , undefined);
}
;
var Lcom_google_gwt_logging_client_ConsoleLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'ConsoleLogHandler', 402);
function DevelopmentModeLogHandler(){
  $setFormatter(this, new TextLogFormatter(false));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(403, 179, $intern_31, DevelopmentModeLogHandler);
_.publish = function publish_0(record){
  return;
}
;
var Lcom_google_gwt_logging_client_DevelopmentModeLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'DevelopmentModeLogHandler', 403);
function $clinit_LogConfiguration(){
  $clinit_LogConfiguration = emptyMethod;
  impl_3 = new LogConfiguration$LogConfigurationImplRegular;
}

function $onModuleLoad_0(){
  var log_0;
  $configureClientSideLogging(impl_3);
  if (!uncaughtExceptionHandler) {
    log_0 = getLogger(($ensureNamesAreInitialized(Lcom_google_gwt_logging_client_LogConfiguration_2_classLit) , Lcom_google_gwt_logging_client_LogConfiguration_2_classLit.typeName));
    setUncaughtExceptionHandler(new LogConfiguration$1(log_0));
  }
}

var impl_3;
var Lcom_google_gwt_logging_client_LogConfiguration_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration', null);
function LogConfiguration$1(val$log){
  this.val$log2 = val$log;
}

defineClass(401, 1, {}, LogConfiguration$1);
_.onUncaughtException = function onUncaughtException_0(e){
  $log(this.val$log2, ($clinit_Level() , SEVERE), e.getMessage(), e);
}
;
var Lcom_google_gwt_logging_client_LogConfiguration$1_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/1', 401);
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

defineClass(400, 1, {}, LogConfiguration$LogConfigurationImplRegular);
var Lcom_google_gwt_logging_client_LogConfiguration$LogConfigurationImplRegular_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/LogConfigurationImplRegular', 400);
function SystemLogHandler(){
  $setFormatter(this, new TextLogFormatter(true));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(404, 179, $intern_31, SystemLogHandler);
_.publish = function publish_1(record){
  return;
}
;
var Lcom_google_gwt_logging_client_SystemLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'SystemLogHandler', 404);
defineClass(1044, 1, {});
var Ljava_util_logging_Formatter_2_classLit = createForClass('java.util.logging', 'Formatter', 1044);
defineClass(1045, 1044, {});
var Lcom_google_gwt_logging_impl_FormatterImpl_2_classLit = createForClass('com.google.gwt.logging.impl', 'FormatterImpl', 1045);
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

defineClass(273, 1045, {}, TextLogFormatter);
_.showStackTraces = false;
var Lcom_google_gwt_logging_client_TextLogFormatter_2_classLit = createForClass('com.google.gwt.logging.client', 'TextLogFormatter', 273);
defineClass(1033, 1, {});
var Ljava_io_OutputStream_2_classLit = createForClass('java.io', 'OutputStream', 1033);
function FilterOutputStream(out){
}

defineClass(320, 1033, {}, FilterOutputStream);
var Ljava_io_FilterOutputStream_2_classLit = createForClass('java.io', 'FilterOutputStream', 320);
function PrintStream(out){
  FilterOutputStream.call(this, out);
}

defineClass(260, 320, {}, PrintStream);
_.println = function println(s){
}
;
var Ljava_io_PrintStream_2_classLit = createForClass('java.io', 'PrintStream', 260);
function StackTracePrintStream(builder){
  PrintStream.call(this, new FilterOutputStream(null));
  this.builder = builder;
}

defineClass(619, 260, {}, StackTracePrintStream);
_.println = function println_0(str){
  $append_5(this.builder, str);
  $append_5(this.builder, '\n');
}
;
var Lcom_google_gwt_logging_impl_StackTracePrintStream_2_classLit = createForClass('com.google.gwt.logging.impl', 'StackTracePrintStream', 619);
function Storage_0(){
  this.storage = 'localStorage';
}

defineClass(556, 1, {}, Storage_0);
var localStorage_1;
var Lcom_google_gwt_storage_client_Storage_2_classLit = createForClass('com.google.gwt.storage.client', 'Storage', 556);
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
  elem == sCaptureElem && $eventGetTypeInt(($clinit_DOMImpl() , evt).type) == 8192 && (sCaptureElem = null);
  listener.onBrowserEvent(evt);
  currentEvent = prevCurrentEvent;
}

function previewEvent(evt){
  $clinit_DOM();
  var ret;
  ret = fire_4(handlers_0, evt);
  if (!ret && !!evt) {
    ($clinit_DOMImpl() , evt).stopPropagation();
    evt.preventDefault();
  }
  return ret;
}

function resolve(maybePotential){
  $clinit_DOM();
  return maybePotential.__gwt_resolve?maybePotential.__gwt_resolve():maybePotential;
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

function addResizeHandler(handler){
  maybeInitializeCloseHandlers();
  maybeInitializeResizeHandlers();
  return addHandler((!TYPE_25 && (TYPE_25 = new GwtEvent$Type) , TYPE_25), handler);
}

function addWindowClosingHandler(handler){
  maybeInitializeCloseHandlers();
  return addHandler(($clinit_Window$ClosingEvent() , $clinit_Window$ClosingEvent() , TYPE_28), handler);
}

function maybeInitializeCloseHandlers(){
  if (!closeHandlersInitialized) {
    $initWindowCloseHandler();
    closeHandlersInitialized = true;
  }
}

function maybeInitializeResizeHandlers(){
  if (!resizeHandlersInitialized) {
    $initWindowResizeHandler();
    resizeHandlersInitialized = true;
  }
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

function onResize_5(){
  var height, width_0;
  if (resizeHandlersInitialized) {
    width_0 = $getClientWidth($doc);
    height = $getClientHeight($doc);
    if (lastResizeWidth != width_0 || lastResizeHeight != height) {
      lastResizeWidth = width_0;
      lastResizeHeight = height;
      fire_2((!handlers_1 && (handlers_1 = new Window$WindowHandlers) , handlers_1));
    }
  }
}

var closeHandlersInitialized = false, handlers_1, lastResizeHeight = 0, lastResizeWidth = 0, resizeHandlersInitialized = false;
function $clinit_Window$ClosingEvent(){
  $clinit_Window$ClosingEvent = emptyMethod;
  TYPE_28 = new GwtEvent$Type;
}

function Window$ClosingEvent(){
  $clinit_Window$ClosingEvent();
}

defineClass(411, 1018, {}, Window$ClosingEvent);
_.dispatch_0 = function dispatch_29(handler){
  var lastArg;
  (lastArg = this , castTo(handler, 1017) , lastArg).message_0 = 'Do you really want to leave the page? You will lose any unsaved changes.';
}
;
_.getAssociatedType = function getAssociatedType_29(){
  return TYPE_28;
}
;
_.message_0 = null;
var TYPE_28;
var Lcom_google_gwt_user_client_Window$ClosingEvent_2_classLit = createForClass('com.google.gwt.user.client', 'Window/ClosingEvent', 411);
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
        if (!instanceOf($e0, 102))
          throw toJs($e0);
      }
      values = castTo(out.get_2(key), 41);
      if (!values) {
        values = new ArrayList;
        out.put(key, values);
      }
      values.add_0(val);
    }
  }
  for (entry$iterator = out.entrySet_0().iterator(); entry$iterator.hasNext_0();) {
    entry = castTo(entry$iterator.next_1(), 45);
    entry.setValue(unmodifiableList(castTo(entry.getValue_0(), 41)));
  }
  out = ($clinit_Collections() , new Collections$UnmodifiableMap(out));
  return out;
}

function ensureListParameterMap(){
  var currentQueryString;
  currentQueryString = $wnd.location.search;
  if (!listParamMap || !$equals_6(cachedQueryString, currentQueryString)) {
    listParamMap = buildListParamMap(currentQueryString);
    cachedQueryString = currentQueryString;
  }
}

function getParameter(name_0){
  var paramsForName;
  ensureListParameterMap();
  paramsForName = castTo(listParamMap.get_2(name_0), 41);
  return !paramsForName?null:castToString(paramsForName.get_0(paramsForName.size_1() - 1));
}

var cachedQueryString = '', listParamMap;
function Window$WindowHandlers(){
  HandlerManager.call(this, null);
}

defineClass(257, 176, $intern_22, Window$WindowHandlers);
var Lcom_google_gwt_user_client_Window$WindowHandlers_2_classLit = createForClass('com.google.gwt.user.client', 'Window/WindowHandlers', 257);
function $eventGetTypeInt(eventType){
  switch (eventType) {
    case 'blur':
      return $intern_32;
    case 'change':
      return 1024;
    case 'click':
      return 1;
    case 'dblclick':
      return 2;
    case 'focus':
      return $intern_33;
    case 'keydown':
      return 128;
    case 'keypress':
      return 256;
    case 'keyup':
      return 512;
    case 'load':
      return $intern_34;
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
      return $intern_35;
    case 'error':
      return $intern_36;
    case 'DOMMouseScroll':
    case 'mousewheel':
      return $intern_37;
    case 'contextmenu':
      return $intern_38;
    case 'paste':
      return $intern_27;
    case 'touchstart':
      return $intern_39;
    case 'touchmove':
      return $intern_40;
    case 'touchend':
      return $intern_28;
    case 'touchcancel':
      return $intern_41;
    case 'gesturestart':
      return $intern_42;
    case 'gesturechange':
      return $intern_43;
    case 'gestureend':
      return $intern_44;
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
  return !instanceOfJso(maybeListener) && instanceOf(maybeListener, 30)?maybeListener:null;
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
  chMask & 1024 && (elem.onchange = bits & 1024?dispatchEvent_2:null);
  chMask & $intern_33 && (elem.onfocus = bits & $intern_33?dispatchEvent_2:null);
  chMask & $intern_32 && (elem.onblur = bits & $intern_32?dispatchEvent_2:null);
  chMask & 8192 && (elem.onlosecapture = bits & 8192?dispatchEvent_2:null);
  chMask & $intern_35 && (elem.onscroll = bits & $intern_35?dispatchEvent_2:null);
  chMask & $intern_34 && (elem.onload = bits & $intern_34?dispatchUnhandledEvent:null);
  chMask & $intern_36 && (elem.onerror = bits & $intern_36?dispatchEvent_2:null);
  chMask & $intern_37 && (elem.onmousewheel = bits & $intern_37?dispatchEvent_2:null);
  chMask & $intern_38 && (elem.oncontextmenu = bits & $intern_38?dispatchEvent_2:null);
  chMask & $intern_27 && (elem.onpaste = bits & $intern_27?dispatchEvent_2:null);
  chMask & $intern_39 && (elem.ontouchstart = bits & $intern_39?dispatchEvent_2:null);
  chMask & $intern_40 && (elem.ontouchmove = bits & $intern_40?dispatchEvent_2:null);
  chMask & $intern_28 && (elem.ontouchend = bits & $intern_28?dispatchEvent_2:null);
  chMask & $intern_41 && (elem.ontouchcancel = bits & $intern_41?dispatchEvent_2:null);
  chMask & $intern_42 && (elem.ongesturestart = bits & $intern_42?dispatchEvent_2:null);
  chMask & $intern_43 && (elem.ongesturechange = bits & $intern_43?dispatchEvent_2:null);
  chMask & $intern_44 && (elem.ongestureend = bits & $intern_44?dispatchEvent_2:null);
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
  dispatchEvent_0(evt, captureElem) && (($clinit_DOMImpl() , evt).stopPropagation() , undefined);
}

function dispatchDragEvent(evt){
  ($clinit_DOMImpl() , evt).preventDefault();
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
  element = ($clinit_DOMImpl() , evt).currentTarget || $wnd;
  element['__gwtLastUnhandledEvent'] = evt.type;
  dispatchEvent_3(evt);
}

function getFirstAncestorWithListener(evt){
  var curElem;
  curElem = ($clinit_DOMImpl() , evt).currentTarget || $wnd;
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

function $initWindowCloseHandler(){
  var oldOnBeforeUnload = $wnd.onbeforeunload;
  var oldOnUnload = $wnd.onunload;
  $wnd.onbeforeunload = function(evt){
    var ret, oldRet;
    try {
      ret = $entry(onClosing)();
    }
     finally {
      oldRet = oldOnBeforeUnload && oldOnBeforeUnload(evt);
    }
    if (ret != null) {
      return ret;
    }
    if (oldRet != null) {
      return oldRet;
    }
  }
  ;
  $wnd.onunload = $entry(function(evt){
    try {
      closeHandlersInitialized && fire_1((!handlers_1 && (handlers_1 = new Window$WindowHandlers) , handlers_1));
    }
     finally {
      oldOnUnload && oldOnUnload(evt);
      $wnd.onresize = null;
      $wnd.onscroll = null;
      $wnd.onbeforeunload = null;
      $wnd.onunload = null;
    }
  }
  );
}

function $initWindowResizeHandler(){
  var oldOnResize = $wnd.onresize;
  $wnd.onresize = $entry(function(evt){
    try {
      onResize_5();
    }
     finally {
      oldOnResize && oldOnResize(evt);
    }
  }
  );
}

function $add_6(this$static, w){
  $add_4(this$static, w, ($clinit_DOM() , this$static.element));
}

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

defineClass(506, 203, $intern_11);
_.remove_2 = function remove_11(w){
  return $remove_5(this, w);
}
;
var Lcom_google_gwt_user_client_ui_AbsolutePanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AbsolutePanel', 506);
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

defineClass(503, 135, $intern_23, AttachDetachException);
var attachCommand, detachCommand;
var Lcom_google_gwt_user_client_ui_AttachDetachException_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException', 503);
function AttachDetachException$1(){
}

defineClass(504, 1, {}, AttachDetachException$1);
_.execute_2 = function execute_30(w){
  w.onAttach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/1', 504);
function AttachDetachException$2(){
}

defineClass(505, 1, {}, AttachDetachException$2);
_.execute_2 = function execute_31(w){
  w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/2', 505);
function LayoutCommand(layout){
  this.layout = layout;
}

defineClass(326, 1, {}, LayoutCommand);
_.doBeforeLayout = function doBeforeLayout(){
}
;
_.execute = function execute_32(){
  this.scheduled = false;
  if (this.canceled) {
    return;
  }
  this.doBeforeLayout();
  $layout(this.layout, this.duration, new LayoutCommand$1);
}
;
_.canceled = false;
_.duration = 0;
_.scheduled = false;
var Lcom_google_gwt_user_client_ui_LayoutCommand_2_classLit = createForClass('com.google.gwt.user.client.ui', 'LayoutCommand', 326);
function LayoutCommand$1(){
}

defineClass(537, 1, {}, LayoutCommand$1);
var Lcom_google_gwt_user_client_ui_LayoutCommand$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'LayoutCommand/1', 537);
function $onResize_0(this$static){
  var child, child$iterator;
  for (child$iterator = new WidgetCollection$WidgetIterator(this$static.children); child$iterator.index_0 < child$iterator.this$01.size_0;) {
    child = $next_0(child$iterator);
    instanceOf(child, 64) && castTo(child, 64).onResize();
  }
}

defineClass(500, 203, $intern_45);
_.onAttach = function onAttach_4(){
  $onAttach(this);
}
;
_.onDetach = function onDetach_3(){
  $onDetach(this);
}
;
_.onResize = function onResize_6(){
  $onResize_0(this);
}
;
_.remove_2 = function remove_14(w){
  var removed;
  removed = $remove_0(this, w);
  removed && $removeChild_0(this.layout, castTo(w.layoutData, 185));
  return removed;
}
;
var Lcom_google_gwt_user_client_ui_LayoutPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'LayoutPanel', 500);
function RootLayoutPanel(){
  ComplexPanel.call(this);
  $setElement(this, $createDivElement($doc));
  this.layout = new Layout(($clinit_DOM() , this.element));
  this.layoutCmd = new LayoutCommand(this.layout);
  addResizeHandler(new RootLayoutPanel$1(this));
}

function get_7(){
  if (!singleton_0) {
    singleton_0 = new RootLayoutPanel;
    $add_6(($clinit_RootPanel() , get_8(null)), singleton_0);
  }
  return singleton_0;
}

defineClass(501, 500, $intern_45, RootLayoutPanel);
_.onLoad = function onLoad_3(){
  $fillParent(this.layout.parentElem);
}
;
var singleton_0;
var Lcom_google_gwt_user_client_ui_RootLayoutPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootLayoutPanel', 501);
function RootLayoutPanel$1(this$0){
  this.this$01 = this$0;
}

defineClass(502, 1, $intern_46, RootLayoutPanel$1);
_.onResize_0 = function onResize_8(event_0){
  $onResize_0(this.this$01);
}
;
var Lcom_google_gwt_user_client_ui_RootLayoutPanel$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootLayoutPanel/1', 502);
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

function detachNow(widget){
  $clinit_RootPanel();
  try {
    widget.onDetach();
  }
   finally {
    $remove_15(widgetsToDetach, widget);
  }
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

function get_8(id_0){
  $clinit_RootPanel();
  var elem, rp;
  rp = castTo($getStringValue(rootPanels, id_0), 204);
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

defineClass(204, 506, $intern_47, RootPanel);
var maybeDetachCommand, rootPanels, widgetsToDetach;
var Lcom_google_gwt_user_client_ui_RootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel', 204);
function RootPanel$1(){
}

defineClass(508, 1, {}, RootPanel$1);
_.execute_2 = function execute_35(w){
  w.isAttached() && w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_RootPanel$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/1', 508);
function RootPanel$2(){
}

defineClass(509, 1, $intern_48, RootPanel$2);
_.onClose = function onClose_0(closeEvent){
  detachWidgets();
}
;
var Lcom_google_gwt_user_client_ui_RootPanel$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/2', 509);
function RootPanel$DefaultRootPanel(){
  RootPanel.call(this, ($clinit_RootPanel() , $doc.body));
}

defineClass(507, 204, $intern_47, RootPanel$DefaultRootPanel);
var Lcom_google_gwt_user_client_ui_RootPanel$DefaultRootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/DefaultRootPanel', 507);
function $add_10(this$static, w){
  $insert_2(this$static, w, this$static.size_0);
}

function $indexOf(this$static, w){
  var i;
  for (i = 0; i < this$static.size_0; ++i) {
    if (this$static.array[i] == w) {
      return i;
    }
  }
  return -1;
}

function $insert_2(this$static, w, beforeIndex){
  var i, i0, newArray;
  if (beforeIndex < 0 || beforeIndex > this$static.size_0) {
    throw toJs(new IndexOutOfBoundsException);
  }
  if (this$static.size_0 == this$static.array.length) {
    newArray = initUnidimensionalArray(Lcom_google_gwt_user_client_ui_Widget_2_classLit, $intern_1, 24, this$static.array.length * 2, 0, 1);
    for (i0 = 0; i0 < this$static.array.length; ++i0) {
      newArray[i0] = this$static.array[i0];
    }
    this$static.array = newArray;
  }
  ++this$static.size_0;
  for (i = this$static.size_0 - 1; i > beforeIndex; --i) {
    this$static.array[i] = this$static.array[i - 1];
  }
  this$static.array[beforeIndex] = w;
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
  this.array = initUnidimensionalArray(Lcom_google_gwt_user_client_ui_Widget_2_classLit, $intern_1, 24, 4, 0, 1);
}

defineClass(625, 1, $intern_49, WidgetCollection);
_.iterator = function iterator_4(){
  return new WidgetCollection$WidgetIterator(this);
}
;
_.size_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection', 625);
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

defineClass(211, 1, {}, WidgetCollection$WidgetIterator);
_.next_1 = function next_1(){
  return $next_0(this);
}
;
_.hasNext_0 = function hasNext_0(){
  return this.index_0 < this.this$01.size_0;
}
;
_.remove_4 = function remove_17(){
  if (!this.currentWidget) {
    throw toJs(new IllegalStateException);
  }
  this.this$01.parent_0.remove_2(this.currentWidget);
  --this.index_0;
  this.currentWidget = null;
}
;
_.index_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection$WidgetIterator_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection/WidgetIterator', 211);
function assertCompileTimeUserAgent(){
  var runtimeValue;
  runtimeValue = $getRuntimeValue();
  if (!$equals_6('safari', runtimeValue)) {
    throw toJs(new UserAgentAsserter$UserAgentAssertionError(runtimeValue));
  }
}

function Error_0(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(153, 17, $intern_50);
var Ljava_lang_Error_2_classLit = createForClass('java.lang', 'Error', 153);
defineClass(91, 153, $intern_50);
var Ljava_lang_AssertionError_2_classLit = createForClass('java.lang', 'AssertionError', 91);
function UserAgentAsserter$UserAgentAssertionError(runtimeValue){
  Error_0.call(this, 'Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (safari) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.' == null?'null':toString_36('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (safari) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.'), instanceOf('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (safari) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 17)?castTo('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (safari) does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 17):null);
}

defineClass(399, 91, $intern_50, UserAgentAsserter$UserAgentAssertionError);
var Lcom_google_gwt_useragent_client_UserAgentAsserter$UserAgentAssertionError_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentAsserter/UserAgentAssertionError', 399);
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

defineClass(415, 1, {}, SimpleEventBus$1);
var Lcom_google_web_bindery_event_shared_SimpleEventBus$1_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/1', 415);
function SimpleEventBus$2(this$0, val$type, val$source, val$handler){
  this.this$01 = this$0;
  this.val$type2 = val$type;
  this.val$source3 = val$source;
  this.val$handler4 = val$handler;
}

defineClass(414, 1, {416:1}, SimpleEventBus$2);
_.execute = function execute_38(){
  $doAddNow(this.this$01, this.val$type2, this.val$source3, this.val$handler4);
}
;
var Lcom_google_web_bindery_event_shared_SimpleEventBus$2_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/2', 414);
function AbstractStringBuilder(string){
  this.string = string;
}

defineClass(232, 1, {309:1});
_.toString_0 = function toString_46(){
  return this.string;
}
;
var Ljava_lang_AbstractStringBuilder_2_classLit = createForClass('java.lang', 'AbstractStringBuilder', 232);
function ArithmeticException(){
  RuntimeException_0.call(this, 'divide by zero');
}

defineClass(438, 10, $intern_5, ArithmeticException);
var Ljava_lang_ArithmeticException_2_classLit = createForClass('java.lang', 'ArithmeticException', 438);
function IndexOutOfBoundsException(){
  RuntimeException.call(this);
}

function IndexOutOfBoundsException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(67, 10, $intern_51, IndexOutOfBoundsException, IndexOutOfBoundsException_0);
var Ljava_lang_IndexOutOfBoundsException_2_classLit = createForClass('java.lang', 'IndexOutOfBoundsException', 67);
function ArrayStoreException(){
  RuntimeException.call(this);
}

defineClass(321, 10, $intern_5, ArrayStoreException);
var Ljava_lang_ArrayStoreException_2_classLit = createForClass('java.lang', 'ArrayStoreException', 321);
function $clinit_Boolean(){
  $clinit_Boolean = emptyMethod;
  FALSE = false;
}

booleanCastMap = {3:1, 315:1, 9:1};
var FALSE;
var Ljava_lang_Boolean_2_classLit = createForClass('java.lang', 'Boolean', 315);
function ClassCastException(){
  RuntimeException_0.call(this, null);
}

defineClass(425, 10, $intern_5, ClassCastException);
var Ljava_lang_ClassCastException_2_classLit = createForClass('java.lang', 'ClassCastException', 425);
defineClass(231, 1, {3:1, 231:1});
var Ljava_lang_Number_2_classLit = createForClass('java.lang', 'Number', 231);
function $equals_4(this$static, o){
  return checkCriticalNotNull(this$static) , this$static === o;
}

function $hashCode_3(this$static){
  return round_int((checkCriticalNotNull(this$static) , this$static));
}

doubleCastMap = {3:1, 9:1, 259:1, 231:1};
var Ljava_lang_Double_2_classLit = createForClass('java.lang', 'Double', 259);
function IllegalArgumentException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(72, 10, $intern_5, IllegalArgumentException_0);
var Ljava_lang_IllegalArgumentException_2_classLit = createForClass('java.lang', 'IllegalArgumentException', 72);
function IllegalStateException(){
  RuntimeException.call(this);
}

function IllegalStateException_0(s){
  RuntimeException_0.call(this, s);
}

defineClass(83, 10, $intern_5, IllegalStateException, IllegalStateException_0);
var Ljava_lang_IllegalStateException_2_classLit = createForClass('java.lang', 'IllegalStateException', 83);
function $equals_5(this$static, o){
  return instanceOf(o, 47) && castTo(o, 47).value_0 == this$static.value_0;
}

function Integer(value_0){
  this.value_0 = value_0;
}

function numberOfLeadingZeros_0(i){
  var m, n, y_0;
  if (i < 0) {
    return 0;
  }
   else if (i == 0) {
    return 32;
  }
   else {
    y_0 = -(i >> 16);
    m = y_0 >> 16 & 16;
    n = 16 - m;
    i = i >> m;
    y_0 = i - 256;
    m = y_0 >> 16 & 8;
    n += m;
    i <<= m;
    y_0 = i - $intern_32;
    m = y_0 >> 16 & 4;
    n += m;
    i <<= m;
    y_0 = i - $intern_35;
    m = y_0 >> 16 & 2;
    n += m;
    i <<= m;
    y_0 = i >> 14;
    m = y_0 & ~(y_0 >> 1);
    return n + 2 - m;
  }
}

function numberOfTrailingZeros(i){
  var r, rtn;
  if (i == 0) {
    return 32;
  }
   else {
    rtn = 0;
    for (r = 1; (r & i) == 0; r <<= 1) {
      ++rtn;
    }
    return rtn;
  }
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

defineClass(47, 231, $intern_52, Integer);
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
var Ljava_lang_Integer_2_classLit = createForClass('java.lang', 'Integer', 47);
function $clinit_Integer$BoxedValues(){
  $clinit_Integer$BoxedValues = emptyMethod;
  boxedValues = initUnidimensionalArray(Ljava_lang_Integer_2_classLit, {3:1, 6:1, 7:1, 253:1, 4:1}, 47, 256, 0, 1);
}

var boxedValues;
defineClass(1118, 1, {});
function NullPointerException(){
  RuntimeException.call(this);
}

function NullPointerException_0(typeError){
  JsException.call(this, typeError);
}

function NullPointerException_1(message){
  RuntimeException_0.call(this, message);
}

defineClass(101, 200, $intern_5, NullPointerException, NullPointerException_0, NullPointerException_1);
_.createError = function createError_0(msg){
  return new TypeError(msg);
}
;
var Ljava_lang_NullPointerException_2_classLit = createForClass('java.lang', 'NullPointerException', 101);
function StackTraceElement(methodName, fileName, lineNumber){
  this.className_0 = 'Unknown';
  this.methodName = methodName;
  this.fileName = fileName;
  this.lineNumber = lineNumber;
}

defineClass(126, 1, {3:1, 126:1}, StackTraceElement);
_.equals_0 = function equals_26(other){
  var st;
  if (instanceOf(other, 126)) {
    st = castTo(other, 126);
    return this.lineNumber == st.lineNumber && this.methodName == st.methodName && this.className_0 == st.className_0 && this.fileName == st.fileName;
  }
  return false;
}
;
_.hashCode_0 = function hashCode_23(){
  return hashCode_28(stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_1, 1, 5, [valueOf_9(this.lineNumber), this.className_0, this.methodName, this.fileName]));
}
;
_.toString_0 = function toString_50(){
  return this.className_0 + '.' + this.methodName + '(' + (this.fileName != null?this.fileName:'Unknown Source') + (this.lineNumber >= 0?':' + this.lineNumber:'') + ')';
}
;
_.lineNumber = 0;
var Ljava_lang_StackTraceElement_2_classLit = createForClass('java.lang', 'StackTraceElement', 126);
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

function fromCharCode(array){
  return String.fromCharCode.apply(null, array);
}

function fromCodePoint(codePoint){
  var hiSurrogate, loSurrogate;
  if (codePoint >= $intern_36) {
    hiSurrogate = 55296 + (codePoint - $intern_36 >> 10 & 1023) & $intern_24;
    loSurrogate = 56320 + (codePoint - $intern_36 & 1023) & $intern_24;
    return String.fromCharCode(hiSurrogate) + ('' + String.fromCharCode(loSurrogate));
  }
   else {
    return String.fromCharCode(codePoint & $intern_24);
  }
}

function valueOf_11(x_0){
  return valueOf_12(x_0, 0, x_0.length);
}

function valueOf_12(x_0, offset, count){
  var batchEnd, batchStart, end, s;
  end = offset + count;
  checkCriticalStringBounds(offset, end, x_0.length);
  s = '';
  for (batchStart = offset; batchStart < end;) {
    batchEnd = $wnd.Math.min(batchStart + 10000, end);
    s += fromCharCode(x_0.slice(batchStart, batchEnd));
    batchStart = batchEnd;
  }
  return s;
}

stringCastMap = {3:1, 309:1, 9:1, 2:1};
var Ljava_lang_String_2_classLit = createForClass('java.lang', 'String', 2);
function $append_1(this$static, x_0){
  this$static.string += String.fromCharCode(x_0);
  return this$static;
}

function $append_3(this$static, x_0){
  this$static.string += '' + x_0;
  return this$static;
}

function $append_4(this$static, x_0){
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

function StringBuilder_0(){
  AbstractStringBuilder.call(this, '');
}

function StringBuilder_1(s){
  AbstractStringBuilder.call(this, (checkCriticalNotNull(s) , s));
}

defineClass(44, 232, {309:1}, StringBuilder, StringBuilder_0, StringBuilder_1);
var Ljava_lang_StringBuilder_2_classLit = createForClass('java.lang', 'StringBuilder', 44);
function StringIndexOutOfBoundsException(message){
  IndexOutOfBoundsException_0.call(this, message);
}

defineClass(318, 67, $intern_51, StringIndexOutOfBoundsException);
var Ljava_lang_StringIndexOutOfBoundsException_2_classLit = createForClass('java.lang', 'StringIndexOutOfBoundsException', 318);
function $clinit_System(){
  $clinit_System = emptyMethod;
  err_0 = new PrintStream(null);
  new PrintStream(null);
}

defineClass(1122, 1, {});
var err_0;
function UnsupportedOperationException(){
  RuntimeException.call(this);
}

function UnsupportedOperationException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(66, 10, $intern_5, UnsupportedOperationException, UnsupportedOperationException_0);
var Ljava_lang_UnsupportedOperationException_2_classLit = createForClass('java.lang', 'UnsupportedOperationException', 66);
function $advanceToFind(this$static, o, remove){
  var e, iter;
  for (iter = this$static.iterator(); iter.hasNext_0();) {
    e = iter.next_1();
    if (maskUndefined(o) === maskUndefined(e) || o != null && equals_Ljava_lang_Object__Z__devirtual$(o, e)) {
      remove && iter.remove_4();
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

defineClass(1034, 1, $intern_53);
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
  return this.toArray_0(initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_1, 1, this.size_1(), 5, 1));
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
var Ljava_util_AbstractCollection_2_classLit = createForClass('java.util', 'AbstractCollection', 1034);
defineClass($intern_54, 1034, $intern_55);
_.equals_0 = function equals_27(o){
  var other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 107)) {
    return false;
  }
  other = castTo(o, 107);
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
var Ljava_util_AbstractSet_2_classLit = createForClass('java.util', 'AbstractSet', $intern_54);
function $contains_3(this$static, o){
  if (instanceOf(o, 45)) {
    return $containsEntry(this$static.this$01, castTo(o, 45));
  }
  return false;
}

function AbstractHashMap$EntrySet(this$0){
  this.this$01 = this$0;
}

defineClass(61, $intern_54, $intern_55, AbstractHashMap$EntrySet);
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
var Ljava_util_AbstractHashMap$EntrySet_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySet', 61);
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
  rv = castTo(this$static.current.next_1(), 45);
  this$static.hasNext = $computeHasNext(this$static);
  return rv;
}

function $remove_9(this$static){
  checkCriticalState(!!this$static.last);
  checkStructuralChange(this$static.this$01, this$static);
  this$static.last.remove_4();
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

defineClass(62, 1, {}, AbstractHashMap$EntrySetIterator);
_.next_1 = function next_2(){
  return $next_1(this);
}
;
_.hasNext_0 = function hasNext_1(){
  return this.hasNext;
}
;
_.remove_4 = function remove_20(){
  $remove_9(this);
}
;
_.hasNext = false;
var Ljava_util_AbstractHashMap$EntrySetIterator_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySetIterator', 62);
defineClass($intern_56, 1034, $intern_57);
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
  if (!instanceOf(o, 41)) {
    return false;
  }
  other = castTo(o, 41);
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
_.remove = function remove_21(index_0){
  throw toJs(new UnsupportedOperationException_0('Remove not supported on this list'));
}
;
var Ljava_util_AbstractList_2_classLit = createForClass('java.util', 'AbstractList', $intern_56);
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

defineClass(109, 1, {}, AbstractList$IteratorImpl);
_.hasNext_0 = function hasNext_2(){
  return $hasNext(this);
}
;
_.next_1 = function next_3(){
  return $next_2(this);
}
;
_.remove_4 = function remove_22(){
  $remove_10(this);
}
;
_.i = 0;
_.last = -1;
var Ljava_util_AbstractList$IteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/IteratorImpl', 109);
function AbstractList$ListIteratorImpl(this$0, start_0){
  this.this$01 = this$0;
  AbstractList$IteratorImpl.call(this, this$0);
  checkCriticalPositionIndex(start_0, this$0.size_1());
  this.i = start_0;
}

defineClass(103, 109, {}, AbstractList$ListIteratorImpl);
_.remove_4 = function remove_23(){
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
var Ljava_util_AbstractList$ListIteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/ListIteratorImpl', 103);
function AbstractMap$1(this$0){
  this.this$01 = this$0;
}

defineClass(120, $intern_54, $intern_55, AbstractMap$1);
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
var Ljava_util_AbstractMap$1_2_classLit = createForClass('java.util', 'AbstractMap/1', 120);
function AbstractMap$1$1(val$outerIter){
  this.val$outerIter2 = val$outerIter;
}

defineClass(137, 1, {}, AbstractMap$1$1);
_.hasNext_0 = function hasNext_3(){
  return this.val$outerIter2.hasNext;
}
;
_.next_1 = function next_4(){
  var entry;
  return entry = $next_1(this.val$outerIter2) , entry.getKey();
}
;
_.remove_4 = function remove_26(){
  $remove_9(this.val$outerIter2);
}
;
var Ljava_util_AbstractMap$1$1_2_classLit = createForClass('java.util', 'AbstractMap/1/1', 137);
function $setValue_0(this$static, value_0){
  var oldValue;
  oldValue = this$static.value_0;
  this$static.value_0 = value_0;
  return oldValue;
}

defineClass(205, 1, {205:1, 45:1});
_.equals_0 = function equals_29(other){
  var entry;
  if (!instanceOf(other, 45)) {
    return false;
  }
  entry = castTo(other, 45);
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
var Ljava_util_AbstractMap$AbstractEntry_2_classLit = createForClass('java.util', 'AbstractMap/AbstractEntry', 205);
function AbstractMap$SimpleEntry(key, value_0){
  this.key_0 = key;
  this.value_0 = value_0;
}

defineClass(156, 205, {205:1, 156:1, 45:1}, AbstractMap$SimpleEntry);
var Ljava_util_AbstractMap$SimpleEntry_2_classLit = createForClass('java.util', 'AbstractMap/SimpleEntry', 156);
defineClass(1046, 1, {45:1});
_.equals_0 = function equals_30(other){
  var entry;
  if (!instanceOf(other, 45)) {
    return false;
  }
  entry = castTo(other, 45);
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
var Ljava_util_AbstractMapEntry_2_classLit = createForClass('java.util', 'AbstractMapEntry', 1046);
function $$init_2(this$static){
  this$static.array = initUnidimensionalArray(Ljava_lang_Object_2_classLit, $intern_1, 1, 0, 5, 1);
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

function $get_6(this$static, index_0){
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

function $remove_12(this$static, o){
  var i;
  i = $indexOf_2(this$static, o, 0);
  if (i == -1) {
    return false;
  }
  checkCriticalElementIndex(i, this$static.array.length);
  removeFrom(this$static.array, i);
  return true;
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

defineClass(11, $intern_56, $intern_58, ArrayList, ArrayList_1);
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
_.get_0 = function get_12(index_0){
  return $get_6(this, index_0);
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
_.remove = function remove_32(index_0){
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

defineClass(12, 1, {}, ArrayList$1);
_.hasNext_0 = function hasNext_6(){
  return this.i < this.this$01.array.length;
}
;
_.next_1 = function next_7(){
  return $next_3(this);
}
;
_.remove_4 = function remove_34(){
  $remove_13(this);
}
;
_.i = 0;
_.last = -1;
var Ljava_util_ArrayList$1_2_classLit = createForClass('java.util', 'ArrayList/1', 12);
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
  return instanceOf(list, 227)?new Collections$UnmodifiableRandomAccessList(list):new Collections$UnmodifiableList(list);
}

var EMPTY_LIST, EMPTY_MAP_0, EMPTY_SET;
function Collections$EmptyList(){
}

defineClass(549, $intern_56, $intern_58, Collections$EmptyList);
_.contains_0 = function contains_8(object){
  return false;
}
;
_.get_0 = function get_14(location_0){
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
var Ljava_util_Collections$EmptyList_2_classLit = createForClass('java.util', 'Collections/EmptyList', 549);
function $clinit_Collections$EmptyListIterator(){
  $clinit_Collections$EmptyListIterator = emptyMethod;
  INSTANCE_32 = new Collections$EmptyListIterator;
}

function Collections$EmptyListIterator(){
}

defineClass(550, 1, {}, Collections$EmptyListIterator);
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
_.remove_4 = function remove_35(){
  throw toJs(new IllegalStateException);
}
;
var INSTANCE_32;
var Ljava_util_Collections$EmptyListIterator_2_classLit = createForClass('java.util', 'Collections/EmptyListIterator', 550);
function Collections$EmptyMap(){
}

defineClass(552, 1038, $intern_13, Collections$EmptyMap);
_.containsKey = function containsKey_3(key){
  return false;
}
;
_.entrySet_0 = function entrySet_2(){
  return $clinit_Collections() , EMPTY_SET;
}
;
_.get_2 = function get_15(key){
  return null;
}
;
_.size_1 = function size_14(){
  return 0;
}
;
var Ljava_util_Collections$EmptyMap_2_classLit = createForClass('java.util', 'Collections/EmptyMap', 552);
function Collections$EmptySet(){
}

defineClass(551, $intern_54, $intern_59, Collections$EmptySet);
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
var Ljava_util_Collections$EmptySet_2_classLit = createForClass('java.util', 'Collections/EmptySet', 551);
defineClass(329, 1, $intern_53);
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
var Ljava_util_Collections$UnmodifiableCollection_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollection', 329);
function $remove_14(){
  throw toJs(new UnsupportedOperationException);
}

function Collections$UnmodifiableCollectionIterator(it){
  this.it = it;
}

defineClass(139, 1, {}, Collections$UnmodifiableCollectionIterator);
_.hasNext_0 = function hasNext_8(){
  return this.it.hasNext_0();
}
;
_.next_1 = function next_9(){
  return this.it.next_1();
}
;
_.remove_4 = function remove_37(){
  $remove_14();
}
;
var Ljava_util_Collections$UnmodifiableCollectionIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollectionIterator', 139);
function Collections$UnmodifiableList(list){
  this.coll = list;
  this.list = list;
}

defineClass(265, 329, $intern_57, Collections$UnmodifiableList);
_.equals_0 = function equals_32(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.list, o);
}
;
_.get_0 = function get_16(index_0){
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
var Ljava_util_Collections$UnmodifiableList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableList', 265);
function Collections$UnmodifiableListIterator(lit){
  Collections$UnmodifiableCollectionIterator.call(this, lit);
  this.lit = lit;
}

defineClass(331, 139, {}, Collections$UnmodifiableListIterator);
_.remove_4 = function remove_39(){
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
var Ljava_util_Collections$UnmodifiableListIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableListIterator', 331);
function $entrySet(this$static){
  !this$static.entrySet && (this$static.entrySet = new Collections$UnmodifiableMap$UnmodifiableEntrySet(this$static.map_0.entrySet_0()));
  return this$static.entrySet;
}

function $get_8(this$static, key){
  return this$static.map_0.get_2(key);
}

function Collections$UnmodifiableMap(map_0){
  this.map_0 = map_0;
}

defineClass(201, 1, $intern_12, Collections$UnmodifiableMap);
_.entrySet_0 = function entrySet_3(){
  return $entrySet(this);
}
;
_.equals_0 = function equals_33(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.map_0, o);
}
;
_.get_2 = function get_17(key){
  return $get_8(this, key);
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
var Ljava_util_Collections$UnmodifiableMap_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap', 201);
defineClass(553, 329, $intern_55);
_.equals_0 = function equals_34(o){
  return equals_Ljava_lang_Object__Z__devirtual$(this.coll, o);
}
;
_.hashCode_0 = function hashCode_33(){
  return hashCode__I__devirtual$(this.coll);
}
;
var Ljava_util_Collections$UnmodifiableSet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableSet', 553);
function $wrap(array, size_0){
  var i;
  for (i = 0; i < size_0; ++i) {
    setCheck(array, i, new Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(castTo(array[i], 45)));
  }
}

function Collections$UnmodifiableMap$UnmodifiableEntrySet(s){
  this.coll = s;
}

defineClass(554, 553, $intern_55, Collections$UnmodifiableMap$UnmodifiableEntrySet);
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
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet', 554);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$1(val$it){
  this.val$it2 = val$it;
}

defineClass(332, 1, {}, Collections$UnmodifiableMap$UnmodifiableEntrySet$1);
_.next_1 = function next_10(){
  return new Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(castTo(this.val$it2.next_1(), 45));
}
;
_.hasNext_0 = function hasNext_9(){
  return this.val$it2.hasNext_0();
}
;
_.remove_4 = function remove_41(){
  throw toJs(new UnsupportedOperationException);
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$1_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/1', 332);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(entry){
  this.entry = entry;
}

defineClass(266, 1, {45:1}, Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry);
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
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/UnmodifiableEntry', 266);
function Collections$UnmodifiableRandomAccessList(list){
  Collections$UnmodifiableList.call(this, list);
}

defineClass(330, 265, {21:1, 51:1, 41:1, 227:1}, Collections$UnmodifiableRandomAccessList);
var Ljava_util_Collections$UnmodifiableRandomAccessList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableRandomAccessList', 330);
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

defineClass(782, 10, $intern_5, ConcurrentModificationException);
var Ljava_util_ConcurrentModificationException_2_classLit = createForClass('java.util', 'ConcurrentModificationException', 782);
function $toString_6(this$static){
  var hourOffset, minuteOffset, offset;
  offset = -this$static.jsdate.getTimezoneOffset();
  hourOffset = (offset >= 0?'+':'') + (offset / 60 | 0);
  minuteOffset = padTwo($wnd.Math.abs(offset) % 60);
  return ($clinit_Date$StringData() , DAYS)[this$static.jsdate.getDay()] + ' ' + MONTHS[this$static.jsdate.getMonth()] + ' ' + padTwo(this$static.jsdate.getDate()) + ' ' + padTwo(this$static.jsdate.getHours()) + ':' + padTwo(this$static.jsdate.getMinutes()) + ':' + padTwo(this$static.jsdate.getSeconds()) + ' GMT' + hourOffset + minuteOffset + ' ' + this$static.jsdate.getFullYear();
}

function Date_0(){
  this.jsdate = new $wnd.Date;
}

function Date_1(date){
  this.jsdate = new $wnd.Date(toDouble_0(date));
}

function padTwo(number){
  return number < 10?'0' + number:'' + number;
}

defineClass(98, 1, $intern_60, Date_0, Date_1);
_.equals_0 = function equals_37(obj){
  return instanceOf(obj, 98) && eq(fromDouble_0(this.jsdate.getTime()), fromDouble_0(castTo(obj, 98).jsdate.getTime()));
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
var Ljava_util_Date_2_classLit = createForClass('java.util', 'Date', 98);
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

function $remove_15(this$static, o){
  return $remove_2(this$static.map_0, o) != null;
}

function HashSet(){
  this.map_0 = new HashMap;
}

defineClass(115, $intern_54, $intern_59, HashSet);
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
var Ljava_util_HashSet_2_classLit = createForClass('java.util', 'HashSet', 115);
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

defineClass(626, 1, $intern_49, InternalHashCodeMap);
_.iterator = function iterator_18(){
  return new InternalHashCodeMap$1(this);
}
;
_.size_0 = 0;
var Ljava_util_InternalHashCodeMap_2_classLit = createForClass('java.util', 'InternalHashCodeMap', 626);
function InternalHashCodeMap$1(this$0){
  this.this$01 = this$0;
  this.chains = this.this$01.backingMap.entries();
  this.chain = new Array;
}

defineClass(344, 1, {}, InternalHashCodeMap$1);
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
_.remove_4 = function remove_43(){
  $remove_16(this.this$01, this.lastEntry.getKey());
  this.itemIndex != 0 && --this.itemIndex;
}
;
_.itemIndex = 0;
_.lastEntry = null;
var Ljava_util_InternalHashCodeMap$1_2_classLit = createForClass('java.util', 'InternalHashCodeMap/1', 344);
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

function $get_9(this$static, key){
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

defineClass(620, 1, $intern_49, InternalStringMap);
_.iterator = function iterator_19(){
  return new InternalStringMap$1(this);
}
;
_.size_0 = 0;
_.valueMod = 0;
var Ljava_util_InternalStringMap_2_classLit = createForClass('java.util', 'InternalStringMap', 620);
function InternalStringMap$1(this$0){
  this.this$01 = this$0;
  this.entries_0 = this.this$01.backingMap.entries();
  this.current = this.entries_0.next();
}

defineClass(343, 1, {}, InternalStringMap$1);
_.next_1 = function next_12(){
  return this.last = this.current , this.current = this.entries_0.next() , new InternalStringMap$2(this.this$01, this.last, this.this$01.valueMod);
}
;
_.hasNext_0 = function hasNext_11(){
  return !this.current.done;
}
;
_.remove_4 = function remove_44(){
  $remove_17(this.this$01, this.last.value[0]);
}
;
var Ljava_util_InternalStringMap$1_2_classLit = createForClass('java.util', 'InternalStringMap/1', 343);
function $getValue_1(this$static){
  if (this$static.this$01.valueMod != this$static.val$lastValueMod3) {
    return $get_9(this$static.this$01, this$static.val$entry2.value[0]);
  }
  return this$static.val$entry2.value[1];
}

function InternalStringMap$2(this$0, val$entry, val$lastValueMod){
  this.this$01 = this$0;
  this.val$entry2 = val$entry;
  this.val$lastValueMod3 = val$lastValueMod;
}

defineClass(621, 1046, {45:1}, InternalStringMap$2);
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
var Ljava_util_InternalStringMap$2_2_classLit = createForClass('java.util', 'InternalStringMap/2', 621);
function $clinit_Locale(){
  $clinit_Locale = emptyMethod;
  ROOT = new Locale$1;
  ENGLISH = new Locale$2;
  defaultLocale = new Locale$4;
}

defineClass(1030, 1, {});
var ENGLISH, ROOT, defaultLocale;
var Ljava_util_Locale_2_classLit = createForClass('java.util', 'Locale', 1030);
function Locale$1(){
}

defineClass(426, 1030, {}, Locale$1);
_.toString_0 = function toString_59(){
  return '';
}
;
var Ljava_util_Locale$1_2_classLit = createForClass('java.util', 'Locale/1', 426);
function Locale$2(){
}

defineClass(427, 1030, {}, Locale$2);
_.toString_0 = function toString_60(){
  return 'en';
}
;
var Ljava_util_Locale$2_2_classLit = createForClass('java.util', 'Locale/2', 427);
function Locale$4(){
}

defineClass(428, 1030, {}, Locale$4);
_.toString_0 = function toString_61(){
  return 'unknown';
}
;
var Ljava_util_Locale$4_2_classLit = createForClass('java.util', 'Locale/4', 428);
function NoSuchElementException(){
  RuntimeException.call(this);
}

defineClass(111, 10, {3:1, 13:1, 10:1, 17:1, 111:1}, NoSuchElementException);
var Ljava_util_NoSuchElementException_2_classLit = createForClass('java.util', 'NoSuchElementException', 111);
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

defineClass(317, 1, {}, StringJoiner);
_.toString_0 = function toString_63(){
  return !this.builder?this.emptyValue:this.suffix.length == 0?this.builder.string:this.builder.string + ('' + this.suffix);
}
;
var Ljava_util_StringJoiner_2_classLit = createForClass('java.util', 'StringJoiner', 317);
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

defineClass($intern_61, 1, $intern_17);
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
var Ljava_util_logging_Level_2_classLit = createForClass('java.util.logging', 'Level', $intern_61);
function Level$LevelAll(){
}

defineClass(513, $intern_61, $intern_17, Level$LevelAll);
_.getName = function getName_18(){
  return 'ALL';
}
;
_.intValue = function intValue_0(){
  return $intern_7;
}
;
var Ljava_util_logging_Level$LevelAll_2_classLit = createForClass('java.util.logging', 'Level/LevelAll', 513);
function Level$LevelConfig(){
}

defineClass(514, $intern_61, $intern_17, Level$LevelConfig);
_.getName = function getName_19(){
  return 'CONFIG';
}
;
_.intValue = function intValue_1(){
  return 700;
}
;
var Ljava_util_logging_Level$LevelConfig_2_classLit = createForClass('java.util.logging', 'Level/LevelConfig', 514);
function Level$LevelFine(){
}

defineClass(515, $intern_61, $intern_17, Level$LevelFine);
_.getName = function getName_20(){
  return 'FINE';
}
;
_.intValue = function intValue_2(){
  return 500;
}
;
var Ljava_util_logging_Level$LevelFine_2_classLit = createForClass('java.util.logging', 'Level/LevelFine', 515);
function Level$LevelFiner(){
}

defineClass(516, $intern_61, $intern_17, Level$LevelFiner);
_.getName = function getName_21(){
  return 'FINER';
}
;
_.intValue = function intValue_3(){
  return 400;
}
;
var Ljava_util_logging_Level$LevelFiner_2_classLit = createForClass('java.util.logging', 'Level/LevelFiner', 516);
function Level$LevelFinest(){
}

defineClass(517, $intern_61, $intern_17, Level$LevelFinest);
_.getName = function getName_22(){
  return 'FINEST';
}
;
_.intValue = function intValue_4(){
  return 300;
}
;
var Ljava_util_logging_Level$LevelFinest_2_classLit = createForClass('java.util.logging', 'Level/LevelFinest', 517);
function Level$LevelInfo(){
}

defineClass(518, $intern_61, $intern_17, Level$LevelInfo);
_.getName = function getName_23(){
  return 'INFO';
}
;
_.intValue = function intValue_5(){
  return 800;
}
;
var Ljava_util_logging_Level$LevelInfo_2_classLit = createForClass('java.util.logging', 'Level/LevelInfo', 518);
function Level$LevelOff(){
}

defineClass(519, $intern_61, $intern_17, Level$LevelOff);
_.getName = function getName_24(){
  return 'OFF';
}
;
_.intValue = function intValue_6(){
  return $intern_0;
}
;
var Ljava_util_logging_Level$LevelOff_2_classLit = createForClass('java.util.logging', 'Level/LevelOff', 519);
function Level$LevelSevere(){
}

defineClass(520, $intern_61, $intern_17, Level$LevelSevere);
_.getName = function getName_25(){
  return 'SEVERE';
}
;
_.intValue = function intValue_7(){
  return $intern_8;
}
;
var Ljava_util_logging_Level$LevelSevere_2_classLit = createForClass('java.util.logging', 'Level/LevelSevere', 520);
function Level$LevelWarning(){
}

defineClass(521, $intern_61, $intern_17, Level$LevelWarning);
_.getName = function getName_26(){
  return 'WARNING';
}
;
_.intValue = function intValue_8(){
  return 900;
}
;
var Ljava_util_logging_Level$LevelWarning_2_classLit = createForClass('java.util.logging', 'Level/LevelWarning', 521);
function $addLoggerImpl(this$static, logger){
  $putStringValue(this$static.loggerMap, ($clinit_Logger() , LOGGING_OFF)?null:logger.name_0, logger);
}

function $ensureLogger(this$static, name_0){
  var logger, newLogger, name_1, parentName;
  logger = castTo($getStringValue(this$static.loggerMap, name_0), 202);
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

defineClass(439, 1, {}, LogManager);
var singleton_1;
var Ljava_util_logging_LogManager_2_classLit = createForClass('java.util.logging', 'LogManager', 439);
function $setLoggerName(this$static, newName){
  this$static.loggerName = newName;
}

function LogRecord(level, msg){
  this.level = level;
  this.msg = msg;
  this.millis_0 = ($clinit_System() , fromDouble_0(Date.now()));
}

defineClass(673, 1, $intern_17, LogRecord);
_.loggerName = '';
_.millis_0 = 0;
_.thrown = null;
var Ljava_util_logging_LogRecord_2_classLit = createForClass('java.util.logging', 'LogRecord', 673);
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
    return initUnidimensionalArray(Ljava_util_logging_Handler_2_classLit, $intern_62, 179, 0, 0, 1);
  }
  return castTo($toArray_1(this$static.handlers, initUnidimensionalArray(Ljava_util_logging_Handler_2_classLit, $intern_62, 179, this$static.handlers.array.length, 0, 1)), 386);
}

function $getLevel_0(this$static){
  return LOGGING_OFF?null:this$static.level;
}

function $isLoggable(this$static, messageLevel){
  return ALL_ENABLED?messageLevel.intValue() >= $getEffectiveLevel(this$static).intValue():INFO_ENABLED?messageLevel.intValue() >= ($clinit_Level() , 800):WARNING_ENABLED?messageLevel.intValue() >= ($clinit_Level() , 900):SEVERE_ENABLED && messageLevel.intValue() >= ($clinit_Level() , $intern_8);
}

function $log(this$static, level, msg, thrown){
  var record;
  (ALL_ENABLED?level.intValue() >= $getEffectiveLevel(this$static).intValue():INFO_ENABLED?level.intValue() >= ($clinit_Level() , 800):WARNING_ENABLED?level.intValue() >= ($clinit_Level() , 900):SEVERE_ENABLED && level.intValue() >= ($clinit_Level() , $intern_8)) && (record = new LogRecord(level, msg) , record.thrown = thrown , $setLoggerName(record, LOGGING_OFF?null:this$static.name_0) , $actuallyLog(this$static, record) , undefined);
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

defineClass(202, 1, {202:1}, Logger);
_.useParentHandlers = false;
var ALL_ENABLED = false, INFO_ENABLED = false, LOGGING_OFF = false, SEVERE_ENABLED = false, WARNING_ENABLED = false;
var Ljava_util_logging_Logger_2_classLit = createForClass('java.util.logging', 'Logger', 202);
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

defineClass(1120, 1, {});
function stampJavaTypeInfo_1(array, referenceType){
  return stampJavaTypeInfo_0(array, referenceType);
}

defineClass(1069, 1, {});
var Ljavaemul_internal_ConsoleLogger_2_classLit = createForClass('javaemul.internal', 'ConsoleLogger', 1069);
function checkCriticalArgument(expression, errorMessage){
  if (!expression) {
    throw toJs(new IllegalArgumentException_0(errorMessage));
  }
}

function checkCriticalArgument_0(expression, errorMessageTemplate, errorMessageArgs){
  if (!expression) {
    throw toJs(new IllegalArgumentException_0(format_0(errorMessageTemplate, errorMessageArgs)));
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

function checkCriticalStringBounds(start_0, end, length_0){
  if (start_0 < 0 || end > length_0 || end < start_0) {
    throw toJs(new StringIndexOutOfBoundsException('fromIndex: ' + start_0 + ', toIndex: ' + end + ', length: ' + length_0));
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

function format_0(template, args){
  var builder, i, placeholderStart, templateStart;
  template = template;
  builder = new StringBuilder_0;
  templateStart = 0;
  i = 0;
  while (i < args.length) {
    placeholderStart = template.indexOf('%s', templateStart);
    if (placeholderStart == -1) {
      break;
    }
    $append_5(builder, template.substr(templateStart, placeholderStart - templateStart));
    $append_4(builder, args[i++]);
    templateStart = placeholderStart + 2;
  }
  $append_5(builder, template.substr(templateStart));
  if (i < args.length) {
    builder.string += ' [';
    $append_4(builder, args[i++]);
    while (i < args.length) {
      builder.string += ', ';
      $append_4(builder, args[i++]);
    }
    builder.string += ']';
  }
  return builder.string;
}

function setPropertySafe(map_0, key, value_0){
  try {
    map_0[key] = value_0;
  }
   catch (ignored) {
  }
}

defineClass(1117, 1, {});
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

defineClass(1054, 1, {387:1});
var Lorg_slf4j_helpers_NamedLoggerBase_2_classLit = createForClass('org.slf4j.helpers', 'NamedLoggerBase', 1054);
defineClass(1055, 1054, {387:1});
var Lorg_slf4j_helpers_MarkerIgnoringBase_2_classLit = createForClass('org.slf4j.helpers', 'MarkerIgnoringBase', 1055);
function $error(this$static, msg){
  $log_0(this$static, ($clinit_Level() , SEVERE), msg, null);
}

function $info(this$static, msg){
  $log_0(this$static, ($clinit_Level() , INFO), msg, null);
}

function $log_0(this$static, level, msg, t){
  $isLoggable(this$static.logger, level) && $log(this$static.logger, level, msg, t);
}

function GWTLoggerAdapter(name_0){
  this.logger = getLogger(name_0);
}

defineClass(674, 1055, {387:1}, GWTLoggerAdapter);
var Lru_finam_slf4jgwt_logging_gwt_GWTLoggerAdapter_2_classLit = createForClass('ru.finam.slf4jgwt.logging.gwt', 'GWTLoggerAdapter', 674);
function $getLogger(this$static, name_0){
  var logger;
  if (name_0 == null) {
    throw toJs(new NullPointerException);
  }
  $equalsIgnoreCase('ROOT', name_0) && (name_0 = '');
  logger = castTo($getStringValue(this$static.loggers, name_0), 387);
  if (!logger) {
    logger = new GWTLoggerAdapter(name_0);
    $putStringValue(this$static.loggers, name_0, logger);
  }
  return logger;
}

function GWTLoggerFactory(){
  this.loggers = new HashMap;
}

defineClass(623, 1, {}, GWTLoggerFactory);
var Lru_finam_slf4jgwt_logging_gwt_GWTLoggerFactory_2_classLit = createForClass('ru.finam.slf4jgwt.logging.gwt', 'GWTLoggerFactory', 623);
function $clinit_Impl_0(){
  $clinit_Impl_0 = emptyMethod;
  LOGGER_FACTORY = new GWTLoggerFactory;
}

var LOGGER_FACTORY;
var C_classLit = createForPrimitive('char', 'C');
var I_classLit = createForPrimitive('int', 'I');
var Z_classLit = createForPrimitive('boolean', 'Z');
var $entry = ($clinit_Impl() , entry_0);
var gwtOnLoad = gwtOnLoad = gwtOnLoad_0;
addInitFunctions(init_2);
setGwtProperty('permProps', [[['locale', 'default'], ['user.agent', 'safari']]]);
$sendStats('moduleStartup', 'moduleEvalEnd');
gwtOnLoad(__gwtModuleFunction.__errFn, __gwtModuleFunction.__moduleName, __gwtModuleFunction.__moduleBase, __gwtModuleFunction.__softPermutationId,__gwtModuleFunction.__computePropValue);
$sendStats('moduleStartup', 'end');
$gwt && $gwt.permProps && __gwtModuleFunction.__moduleStartupDone($gwt.permProps);
//# sourceURL=baseletgwt-0.js

