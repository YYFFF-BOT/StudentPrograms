gradle clean build && \
gradle test jacocoTestReport && \
open ./app/build/reports/jacoco/test/html/index.html -a Google\ Chrome

echo ""
echo "If Gradle Test fails, Jacoco Test Report will not be created."
echo "If Jacoco Test Report has been created and you don't have Google Chrome,"
echo "you need to manually open app/build/reports/jacoco/test/html/index.html"
echo "using your own web browser."

bash reset_json.sh
