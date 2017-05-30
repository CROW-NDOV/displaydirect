cd ../src/main/resources/proto/
protoc --java_out=../../../../src/main/java/ display_direct.proto
protoc --python_out=../../../../../py_virtual_screen display_direct.proto
