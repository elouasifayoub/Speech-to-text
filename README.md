## Google Cloud Text-to-Speech: Spring boot <br />
Using the power of ffmpeg/flac/Google and Spring boot here is a simple interface to convert speech to text.

Using a new speech API from Google with the help of this article: https://cloud.google.com/speech-to-text/docs/transcribe-client-libraries

## Quickstart <br />
### Step 1: <br />
1- Log on to the Google Cloud Platform console https://console.cloud.google.com <br />
2- Enable Speech-to-Text on a GCP project. (Make sure billing is enabled for Speech-to-Text) <br />
![CHEESE!](images/screen1.JPG)

3 - Enable Cloud Translation API. <br />
![CHEESE!](images/screen2.JPG)

### Step 2: Creating a Service Account Key in the Google Cloud Platform project <br />
1 - Select the project that you want to monitor. Note down the project ID. <br />
In this example, the project ID is gcp-mon-dev <br />
2 - From the left navigation pane, select IAM  and  admin > Service Accounts <br />
![CHEESE!](images/screen3.JPG)

3 - On the Service Accounts page, select CREATE SERVICE ACCOUNT. The Create service account dialog box is displayed. Enter the account name. In Role, select the Project > role. <br />
![CHEESE!](images/screen4.JPG)

4 - Select the Furnish a new private key check box.  Select the JSON Key type. Click CREATE. <br />
![CHEESE!](images/screen5.JPG)

5 - The Service account file is created and downloaded on the computer. Copy this JSON file and place it in the Speech-to-text\src\main\resources directory on the Project <br />
![CHEESE!](images/screen6.JPG)

6 – Open application.properties and put the name of your JSON file <br />
![CHEESE!](images/screen7.JPG)

### Step 3: Create A API key <br />
1 - On GGP home, click on the Navigation Menu. <br />
Go to APIs & Services option and click on Credentials. <br />
![CHEESE!](images/screen8.JPG)

Now click on +CREATE CREDENTIALS. <br />
![CHEESE!](images/screen9.JPG)

Choose the option API KEY <br />
![CHEESE!](images/screen10.JPG)

Copy the key <br />
![CHEESE!](images/screen11.JPG)

Paste it here <br />
![CHEESE!](images/screen12.JPG)

### Step 4: Create New Bucket <br />
Go to the Navigation Menu > Storage > Browser. <br />
![CHEESE!](images/screen13.JPG)

Click on Create Bucket to create a new bucket. <br />
Copy and paste your bucket name here. <br />
![CHEESE!](images/screen14.JPG)

## Run the Spring Boot application <br />
![CHEESE!](images/screen15.JPG)

Now, open the browser and invoke the URL http://localhost:8777 <br />
![CHEESE!](images/screen17.JPG)



