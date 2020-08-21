## Sample Configuration File

```yaml
# Edition supported. One of [bedrock, education]. Default: bedrock
edition: education

# Don't touch
version: 1
```

## Configuration

### edition
Set the edition the server will listen for. This can be either `bedrock` or `education`. As both editions
are not compatible they cannot listen on the same port.
