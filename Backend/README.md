# Backend API

## API url

### Heroku Production Environment
https://dashboard.heroku.com/apps/sharedagendaapp

### Local Host Developing Environment
localhost:8080 or localhost:$PORT (if $PORT set in system)

## Example Usage
### with curl or Postman:
curl -i localhost:8080/getAccount -H Content-Type:application/json -X POST --data '{"accountId": "yourId"}'


## Current API Endpoints:
```
/createAccount:	
	{
		"accountId": string,
		"nickname": string,
		"description": string[optional]
	}

/updateAccount:
	{
		"accountId": string,
		"nickname": string,
		"description": string[optional]
	}

/getAccount:
	{
		"accountId": string
	}
```

## Developing Process
1. Create .yaml files under /Backend/schema/src/ for java types/request/response object
2. Build schema submodule to convert yaml files to pojo
3. Add new controller under /Backend/service/src/main/java/controller to listen on new endpoint
4. Modify database in DataStore.java to extend database usability
5. cd Backend && ./gradlew clean build run
6. Now api service should be listen on the default port 8080 or $PORT if set
 


## Controller Example:
see [CreateAccountController](https://github.com/sudojimmy/Shared-Agenda-Application/blob/master/Backend/service/src/main/java/controller/CreateAccountController.java)


## Useful Tutorial Document:
[Spring REST server tutorial](https://spring.io/guides/gs/rest-service/)

[Google Authentication](https://developers.google.com/identity/sign-in/android/start)

