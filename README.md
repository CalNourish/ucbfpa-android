# CalNourish

This is the repo for the CalNourish mobile application for **android devices**. This application is being made for the UC Berkeley Food Pantry.

API 28

Target SDK: 28

min SDK: 23

## Setting up
1. Clone the repo
2. Obtain the firebase key from current developers
3. yeet


## Work flow
1. Create your own development branch
2. If you want to experiment, create another branch so we don't break things
3. When you feel comfortable, create a PR from your branch to master

## Testing
To see testing updates, visit the [wiki](https://github.com/CalNourish/ucbfpa-android/wiki)


## Deploying + DeployGate

### PART 1: The Deployment
**When deploying, make sure you are using the `google-services.json` from prod**

**Option 1:**
Deploy from Android Studio to the DeployGate distribution by changing the Run/Debug config dialog on the top toolbar from `ucbfpa-android:app [build]` to
`ucbfpa-android:app [uploadDeployGateDebug]` and then hit the green build/play button to the right of the dialog box.

**Option 2:**
Run `./gradlew uploadDeployGateDebug`, which will automatically build and deploy to DeployGate.
If there is authentication error, run `./gradlew loginDeployGate` and try the first command again

### PART 2: DeployGate
1. Once you've built and deployed to DeployGate. Head to the DeployGate dashboard
2. Under "Distributions" on the right, click the settings button for **UC Berkeley Food Pantry App** and select **Update Distribution**
3. For "Update Version," select the version you just built and deployed
4. Click **Update Distribution**

## Project Stuff
1. MainActivity = CategoryActivity = our main screen when app appears
2. Bottom bar has:
	* Info
		+ just textbox with open hours and contancts and etc.
	* Category
		+ buttons that points to new activity with those items
	* Search
		+ goes to search bar now -- need to do the query stuff and history keeping etc.
	* Food Recovery
		+ empty
	* Menu
		+ has buttons that points to activities (has info category search for now)



