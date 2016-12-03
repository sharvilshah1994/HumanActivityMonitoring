//==========================================================================
// 2015/08/31: yctung: add this new test for libSVM in jni interface 
//==========================================================================

#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include "./libsvm/svm-train.h"
#include "./libsvm/svm-predict.h"
#include "common.h"
#include <sstream>



// helper function to be called in Java for making svm-train
extern "C" void Java_com_mc_rkotwal2_assignment3_MainActivity_jniSvmTrain(JNIEnv *env, jobject obj, jstring cmdIn){
	const char *cmd = env->GetStringUTFChars(cmdIn, 0);
	debug("jniSvmTrain cmd = %s", cmd);

	std::vector<char*> v;

	// add dummy head to meet argv/command format
	std::string cmdString = std::string("dummy ")+std::string(cmd);

	cmdToArgv(cmdString, v);

	// make svm train by libsvm
	svmtrain::main(v.size(),&v[0]);


	// free vector memory
	for(int i=0;i<v.size();i++){
		free(v[i]);
	}

	// free java object memory
	env->ReleaseStringUTFChars(cmdIn, cmd);
}

// helper function to be called in Java for making svm-predict
extern "C" jstring Java_com_mc_rkotwal2_assignment3_MainActivity_jniSvmPredict(JNIEnv *env, jobject obj, jstring cmdIn){
	const char *cmd = env->GetStringUTFChars(cmdIn, 0);
	debug("jniSvmPredict cmd = %s", cmd);

	std::vector<char*> v;

	// add dummy head to meet argv/command format
	std::string cmdString = std::string("dummy ")+std::string(cmd);

	cmdToArgv(cmdString, v);

	// make svm train by libsvm
	double ans=svmpredict::main(v.size(),&v[0],1);
	double correct=svmpredict::main(v.size(),&v[0],2);
	double total=svmpredict::main(v.size(),&v[0],3);

	std::ostringstream strs;
	strs << ans;
	std::string str1 = strs.str();

	std::ostringstream strs1;
	strs1 << correct;
	std::string str2 = strs1.str();

	std::ostringstream strs2;
	strs2 << total;
	std::string str3 = strs2.str();

	std::string str4=str1+"% ("+str2+"/"+str3+") (classification)";

	// free vector memory
	for(int i=0;i<v.size();i++){
		free(v[i]);
	}

	// free java object memory
	env->ReleaseStringUTFChars(cmdIn, cmd);
	return env->NewStringUTF(str4.c_str());
	//return str4;
}



/*
*  just some test functions -> can be removed
*/
extern "C" JNIEXPORT int JNICALL Java_com_mc_rkotwal2_assignment3_MainActivity_testInt(JNIEnv * env, jobject obj){
	return 5566;
}

extern "C" void Java_com_mc_rkotwal2_assignment3_MainActivity_testLog(JNIEnv *env, jobject obj, jstring logThis){
	const char * szLogThis = env->GetStringUTFChars(logThis, 0);
	debug("%s",szLogThis);

	env->ReleaseStringUTFChars(logThis, szLogThis);
} 
