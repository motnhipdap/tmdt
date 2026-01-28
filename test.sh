# Chạy tất cả tests
./mvnw test

# Chạy tests cho module authorization
#./mvnw test -Dtest=com.dev.dungcony.modules.authorization.**

# Chạy một test class cụ thể
# ./mvnw test -Dtest=AuthServiceImplTest

# Chạy một test method cụ thể
# ./mvnw test -Dtest=AuthServiceImplTest#login_Success