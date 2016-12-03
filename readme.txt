This project is submitted by following group members:

Sharvil Shah - sdshah10@asu.edu
Rashik Kotwal - rashik.kotwal@asu.edu
Palak Anmol - panmol@asu.edu
Saakethchandra Muppana - smuppana@asu.edu


Steps to run this project : 

Download assignment3.
Import folder assignment3 on Android Studio. Or simply open the project on Android Studio.
Now tap Run button on task bar and select the android device where you want to run the android code./  Install .apk file to your device.



Part A:
Please find the Kotwal.db in the zipped folder.
There are three tables in this database: 
1. 'Patient_data' (which is total dataset)
2. 'training_data' (which is 2/3rd of the total dataset)
3. 'testing data' (which is 1/3rd of the total dataset)

Also the corresponding data in scaled format(index-value pair format) in the 'data' folder.
(file names : heart_scale1,heart_scale_predict1,heart_scale_train1)
Labels used:
+1 : Eating 
+2 : Walking 
+3 : Running

Note : 50 set rows(X,Y,Z acceleration values) correspond to one activity.
So 50 x 20 = 1000 rows for Eating, Running and Walking each.
An approx. 3000 rows of data were created in total.

Part B:

1. Choose the kernel type for SVM classification - Radial basis or Sigmoid
2. Press the Make the training model button to create the model file.
3. Then press the Predict accuracy button to print the accuracy on screen.

Note : the accuracies will come different for different kernel types.
