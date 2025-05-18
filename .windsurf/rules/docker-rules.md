---
trigger: always_on
---

# Docker Compose Configuration Rules

## Application Service Requirement
- Always include the application service in docker-compose.yml
- The application service must:
  - Depend on all required infrastructure services (database, cache, etc.)
  - Include proper health checks
  - Have necessary environment variables configured
  - Mount the application code for development
  - Set proper working directory
  - Include proper volume mounts for development tools (e.g., Maven cache)
  - Configure proper port mappings
  - Set appropriate resource limits if needed

## Service Dependencies
- Use `depends_on` with `condition: service_healthy` for all service dependencies
- Ensure proper startup order using `depends_on`
- Include health checks for all services

## Environment Variables
- Use environment variables for all configuration
- Provide default values where appropriate using `${VARIABLE:-default}` syntax
- Document all environment variables in the docker-compose file

## Volumes
- Use named volumes for persistent data
- Mount application code for development
- Cache build dependencies to speed up builds

## Health Checks
- Include health checks for all services
- Use appropriate intervals and timeouts
- Set proper start periods for services that take time to initialize

## Docker Configuration Best Practices
- Use multi-stage Docker builds for smaller images
- Specify exact versions for base images
- Use minimal base images when possible
- Run containers as non-root users
- Include proper HEALTHCHECK instructions
- Set appropriate resource limits
- Use .dockerignore to exclude unnecessary files
- Layer images efficiently to leverage caching
- Include proper labels for metadata
- Set proper environment variables

## Docker Compose Network Configuration
- Use custom networks for service isolation
- Configure proper network aliases
- Expose only necessary ports
- Use internal networks for inter-service communication

## Docker Security Best Practices
- Never store secrets in Docker images
- Use Docker secrets or environment variables for sensitive data
- Scan images for vulnerabilities
- Keep base images updated
- Limit container capabilities
- Use read-only file systems where possible
- Implement proper logging for container activities
