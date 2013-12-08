#prefix= @prefix@
#INSTALL = @INSTALL@
TARGET= GetAppInfo.class GetPackages.class

.SUFFIXES: .built

all: $(TARGET)
	@mkdir -p pkgs

GetAppInfo.class: GetAppConfig.class GetAppInfo.java
	javac -cp lib/androidmarketapi-0.6.jar:lib/protobuf-java-2.2.0.jar:lib/jackson-core-2.3.0.jar:. GetAppInfo.java

GetPackages.class: GetAppConfig.class GetPackages.java
	javac -cp lib/AndroidMarketApi.jar:lib/protobuf-java-2.4.1.jar:lib/jackson-core-2.3.0.jar:. GetPackages.java

GetAppConfig.class: 
	javac -cp lib/jackson-core-2.3.0.jar:. GetAppConfig.java

clean:
	-test -z "$(TARGET)" || rm -rf *.class
