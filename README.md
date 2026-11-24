# Student Management Web Application on AWS (2-Tier)

A portfolio-ready Java web application based on the project outline supplied by the user. It runs on **Apache Tomcat on EC2** and stores student records in **Amazon RDS MySQL/MariaDB**.

## Architecture

```text
Browser
  |
  | HTTP :8080
  v
EC2 (Amazon Linux + Java + Apache Tomcat)
  |  Student JSP/Servlet application
  |  JDBC over TCP :3306
  v
Amazon RDS (MySQL / MariaDB)
```

## Features

- Add student records
- List and search records by student name or qualification
- Delete a record
- JDBC + prepared statements
- Responsive web UI
- Credentials read from environment variables (no passwords committed)

## Tech stack

Java 11, Maven, Servlet 4, JSP/JSTL, Apache Tomcat 9, MySQL Connector/J, Amazon EC2, Amazon RDS.

## 1. Database setup

Create an RDS MySQL or MariaDB instance. For the database security group, allow inbound TCP **3306 only from the EC2 application's security group** (not from `0.0.0.0/0`).

Run the schema:

```bash
mysql -h YOUR-RDS-ENDPOINT -u YOUR_DB_USERNAME -p --ssl
```

Then in the MySQL prompt:

```sql
source sql/schema.sql
```

If using a database user other than the RDS master user, grant it only the required privileges on `studentapp.*`.

## 2. Build the WAR

Install Java 11+ and Maven, then from the project root:

```bash
mvn clean package
```

The WAR is created at `target/student.war`.

## 3. Deploy to EC2 / Tomcat 9

On Amazon Linux, install Java and download Tomcat 9 from the official Apache Tomcat website. Extract it under `/opt/tomcat` (or your chosen directory).

Copy the WAR:

```bash
sudo cp target/student.war /opt/tomcat/webapps/student.war
```

Set the database environment variables for the Tomcat service:

```bash
export DB_URL='jdbc:mysql://YOUR-RDS-ENDPOINT:3306/studentapp?useSSL=true&serverTimezone=UTC'
export DB_USER='YOUR_DB_USERNAME'
export DB_PASSWORD='YOUR_DB_PASSWORD'
```

For a persistent service, configure these variables in a protected systemd EnvironmentFile, readable only by root. Do not put credentials in GitHub or in the README.

Restart Tomcat and open:

```text
http://YOUR-EC2-PUBLIC-IP:8080/student/students
```

## 4. Security group checklist

- EC2 inbound TCP 22: restrict to your own IP
- EC2 inbound TCP 8080: your IP for testing; use a load balancer/HTTPS for a production deployment
- RDS inbound TCP 3306: source should be the EC2 security group
- Keep the RDS database private
- Do not commit `.env`, passwords, private keys, or AWS access keys

## Troubleshooting

- **HTTP 404**: confirm `student.war` is deployed and the URL includes `/student/students`.
- **Database connection error**: verify `DB_URL`, `DB_USER`, `DB_PASSWORD`, RDS endpoint, DB security group, and that RDS is reachable from EC2.
- **Access denied**: check the database username/password and grants for `studentapp`.
- **Tomcat deployment failure**: inspect Tomcat `logs/catalina.out` and the application log.
- **JDBC driver error**: confirm Maven packaged MySQL Connector/J in `WEB-INF/lib`.

## GitHub upload

1. Create a new **public** GitHub repository named `student-management-aws` (or choose private if preferred).
2. Extract this ZIP on your computer.
3. Open PowerShell in the extracted project folder.
4. Run:

```powershell
git init
git add .
git commit -m "Add AWS 2-tier student management project"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/student-management-aws.git
git push -u origin main
```

Replace `YOUR-USERNAME` with your GitHub username. GitHub may ask you to sign in through your browser.

## Project note

This is an original source-code implementation of the architecture and features described in the provided project PDF. It is not the same prebuilt `student.war` referenced by that PDF. Test the Maven build and AWS deployment in your own account before presenting it as deployed.
