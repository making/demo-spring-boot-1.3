# Demo OAuth2


## Secured resource

```
$ curl localhost:8080 | jq .
{
  "error": "unauthorized",
  "error_description": "Full authentication is required to access this resource"
}
```

## Issue access token

```
$ curl foo:bar@localhost:8080/oauth/token -d grant_type=password -d username=user -d password=password -d scope=read | jq .
{
  "access_token": "43a001d3-862f-4f02-a60f-b04d0988c24c",
  "token_type": "bearer",
  "refresh_token": "e4af4aac-eb8e-423b-b3a9-a73ba0f81fd1",
  "expires_in": 43199,
  "scope": "read"
}
```

## Access resource

```
$ curl -H "Authorization: Bearer 43a001d3-862f-4f02-a60f-b04d0988c24c" localhost:8080
Hello OAuth2!
```