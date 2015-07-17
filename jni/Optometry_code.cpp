/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <Optometry_code.h>
#include <android/log.h>
#include <stdio.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"Tag",__VA_ARGS__);



/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */


JNIEXPORT jbyteArray JNICALL Java_com_rs_optometry_1gateway_Optometry_1code_TranInt2Short
  (JNIEnv *env, jclass obj, jint mData)
{
	 jbyteArray result=env->NewByteArray(2);
	 jshort *dest =(jshort *)env->GetByteArrayElements(result, 0);

	 unsigned short tran_data=mData>=0?mData&0x7fff:((mData*-1)&0x7fff)|0x8000;

	 *dest=tran_data;

	 //LOGI("inputdata: %d  0x%x  transhort: 0x%x",mData,mData,tran_data);
	 //LOGI("tran data: 0x%x 0x%x",dest[0],dest[1]);

	 return result;
}

JNIEXPORT jbyte JNICALL Java_com_rs_optometry_1gateway_Optometry_1code_TranInt2Char
  (JNIEnv *env, jclass obj, jint mData)
{
	jbyte result;

	result=mData>=0?mData&0x7f:((mData*-1)&0x7f)|0x80;

	return result;
}


