# scanning picking

## 📜 Project description
### 💼 Use case
The core use case of this project is to explore the available features of Vaadin
and try to see what benefits they can possibly bring to the scanning process that H.Essers currently uses.

### 🧬 Features
> 📷 Scanning a barcode and updating the picking & movement status </br>
> ⚡ Automatically get movement assigned on choosing company and action </br>
> 📦 Reporting damage to a movement and being able to take pictures </br>
> 📱  PWA that works on any android device </br>
> ⛔ Not reliant on an internet connection </br>

### ⌛ Not implemented & possible implementations
* Operational on a non scanner device
  * use a package like [ZXingVaadin](https://vaadin.com/directory/component/zxingvaadin) that uses the native camera to detect barcodes
* Not reliant on an internet connection
  * Use a synchronization method that detects changes to network connection, if it detects a network, it will try to synchronize with the server and store necessary data locally.
  * When there is no connection, it will use the local data to fill the UI and queue the data to be sent to the server when the connection is available again.

## 💻 Development

### 🧱 Tech stack
> <b>Programming language:</b> ☕ Java </br>
> <b>Database:</b> 💾 PostgreSQL </br> 
> <b>Frontend:</b> 🦌 Vaadin </br>
> <b>Backend: </b> 🍃 Spring Boot </br>

### 📦 Dependencies
> 🦌 [Vaadin](https://vaadin.com/): to build the UI</br>
> 🍃 [Spring Boot](https://spring.io/projects/spring-boot): to build the backend</br>
> 💧 [Liquibase](https://www.liquibase.org/): Database version control</br>
> 💾 [PostgreSQL](https://www.postgresql.org/): Database</br>
> 🔒 [Spring Security](https://spring.io/projects/spring-security): Security</br>

### 🛠️ Development environment
> 🐳 [Docker](https://www.docker.com/): Containerize the application</br>
> 📦 [Maven](https://maven.apache.org/): Build/Package the application</br>
> ♻ [Git](https://git-scm.com/): Version control</br>
> 🖥️[IntelliJ IDEA](https://www.jetbrains.com/idea/): IDE</br>


<p>✏️<b>Created by Ferre Donné - ferdon</b></p>
