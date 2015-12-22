# Introduction #

Currently, there is a single Demo Application:

`com.sri.ai.grinder.demo.RewriteSystemDemoApp`

that can be deployed as a Java Web Stat Application.


# Details #
To update the demo(s) the following steps need to be performed:
  * Checkout: https://aic-expresso.googlecode.com/svn/demo
    * Under your local workspace as 'aic-expresso-demo'
  * From your aic-expresso workspace run the command:
    * mvn package
  * This will create a file named (in the /target directory):
    * aic-expresso-<Current Version>-jar-with-dependencies.jar
  * Copy this file over to the aic-expresso-demo project and rename it to (replacing the same named file):
    * aic-expresso-jar-with-dependencies.jar
  * Signing the jar:
    * Create the keystore (from the command line):
      * keytool -genkey -keystore c:/temp/aicexpresso.keystore -alias aicexpresso
      * > Enter keystore password: <Enter an appropriate password>
      * > Re-enter new password: <Same password>
      * > What is your first and last name?
        * [_Unknown_]: <Your Name>
      * > What is the name of your organizational unit?
        * [_Unknown_]: AIC-Expresso Open Source Project
      * > What is the name of your organization?
        * [_Unknown_]: AIC-Expresso Open Source Project
      * > What is the name of your City or Locality?
        * [_Unknown_]: Menlo Park
      * > What is the name of your State or Province?
        * [_Unknown_]: California
      * > What is the two-letter country code for this unit?
        * [_Unknown_]: US
      * > ...
        * [_no_]: yes
    * Sign the Jar:
      * jarsigner -keystore c:/temp/aicexpresso.keystore aic-expresso-jar-with-dependencies.jar aicexpresso
  * Commit the aic-expresso-demo changes to the repository and ensure the updates are reflected in the newly deployed demo:
    * http://aic-expresso.googlecode.com/svn/demo/aicexpressorewritedemo.html