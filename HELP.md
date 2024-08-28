# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.3/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.3/reference/htmlsingle/index.html#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


### Database setup

```
DROP DATABASE IF EXISTS booking_dev;
CREATE DATABASE booking_dev DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;
```


**Datasbase User and grant**

```
CREATE USER 'booking'@localhost IDENTIFIED BY 'secret';
CREATE USER 'booking'@'%' IDENTIFIED BY 'secret';

GRANT ALL PRIVILEGES ON `booking_dev`.* TO 'booking'@localhost;
GRANT ALL PRIVILEGES ON `booking_dev`.* TO 'booking'@'%';
GRANT ALL PRIVILEGES ON `booking_dev`.* TO 'booking'@localhost IDENTIFIED BY 'secret';
```

### Setup keystore

#### Create PKCS#12 keystore (.p12 or .pfx file)

```
keytool -genkeypair -keystore myKeystore.p12 -storetype PKCS12 -storepass MY_PASSWORD \
  -alias KEYSTORE_ENTRY -keyalg RSA -keysize 2048 -validity 99999 \
  -dname "CN=My SSL Certificate, OU=My Team, O=My Company, L=My City, ST=My State, C=SA" -ext san=dns:mydomain.com,dns:localhost,ip:127.0.0.1
```
Ex:

```
keytool -genkeypair -keystore rocstar-api.p12 -storetype PKCS12 -storepass secret \
  -alias rocstarapi -keyalg RSA -keysize 2048 -validity 99999 \
  -dname "CN=SYNC API SSL Certificate, OU=My Team, O=My Company, L=My City, ST=My State, C=SA" -ext san=dns:localhost,dns:rocstar.exysoft.com,ip:127.0.0.1

myKeystore.p12 = keystore filename. It can with .pfx extension as well.
MY_PASSWORD = password used for the keystore and the private key as well.
CN = commonName, it will be shown as certiciate name in certificates list.
OU = organizationUnit, department name for example.
O = organizationName, the company name.
L = localityName, the city.
S = stateName, the state.
C = country, the 2-letter code of the country.
```

**Generate key pair for JWT**

```
keytool -genkeypair -keystore booking-jwt.p12 -storetype PKCS12 -storepass secret \
  -alias jwt -keyalg RSA -keysize 2048 -validity 9999 \
  -dname "CN=Booking API Certificate, OU=BOOKING API, O=TUANCUONG, L=HCM, ST=HCM, C=VN"
```

Note: This step can be done using openssl but it's more complicated.

**Create the public certificate (has the header -----BEGIN CERTIFICATE-----)**

Using keytool:

```
keytool -exportcert -keystore booking-jwt.p12 -storepass secret -alias jwt -rfc -file booking-jwt.pem
```

Or using openssl:

```
openssl pkcs12 -in myKeystore.p12 -password pass:MY_PASSWORD -nokeys -out public-certificate.pem

Note: Import public-certificate.pem into browsers to trust it. Add it to "Trusted Root Certification Authorities" certificate store.
```

**Export the private key (has the header -----BEGIN PRIVATE KEY-----)**


```
openssl pkcs12 -in myKeystore.p12 -password pass:MY_PASSWORD -nodes -nocerts -out private-key.key
```

or:

```
openssl pkcs12 -in keystore_name.p12 -nodes -nocerts -out private.key.
```

**Export the public key from the private key (has the header -----BEGIN PUBLIC KEY-----)**

```
openssl pkcs12 -in keystore_name.p12 -nokeys -out public-cert-file.
```
