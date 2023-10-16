# demo-cardtokens-java

## Introduction
This example, using Java, shows how to create a token towards the Cardtokens API, create a cryptogram, get status and delete the Token. 

You can run this code directly using a predefined apikey, merchantid and certificate. You can also get a FREE test account and inject with your own apikey, merchantid and certificate. Just visit https://www.cardtokens.io

## Steps to run this Java code

### Install Java
```bash
sudo apt install default-jdk
```

### Clone repo
```bash
git clone https://github.com/cardtokens/demo-cardtokens-java.git
```

### Navigate to folder locally
```bash
cd demo-cardtokens-java
```

### Build
```bash
sudo javac -cp "lib/*" Cardtokens.java CardtokensEncryptor.java CardtokensHttp.java CardtokensResponse.java CardType.java CryptogramResponseType.java DeleteResponseType.java StatusResponseType.java TokenResponseType.java TokenType.java
```

### Create Jar
```bash
sudo jar cfm Cardtokens.jar manifest.MF *.class
```

### Run
```bash
java -cp Cardtokens.jar:lib/* Cardtokens
```
