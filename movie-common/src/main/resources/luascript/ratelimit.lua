local key = KEYS[1]
local limit = ARGV[1]
local retryAfter = ARGV[2]
local ttlSeconds = ARGV[3]

local remaining = redis.call("HGET", key, "remaining")
if remaining == false then
  -- 초기화
  redis.call("HSET", key, "remaining", limit - 1)
  redis.call("HSET", key, "retryAfter", retryAfter)
  redis.call("EXPIRE", key, ttlSeconds)
  return {1, limit - 1, 0} -- 성공, 남은 요청, retryAfter 0 전달
end

remaining = tonumber(remaining)
if remaining <= 0 then
  local getRetryAfter = redis.call("HGET", key, "retryAfter")
  return {0, 0, getRetryAfter}  -- 실패, 남은 요청 0, retryAfter 전달
else
  redis.call("HINCRBY", key, "remaining", -1)
  return {1, remaining - 1, 0}  -- 성공, 남은 요청, retryAfter 0 전달
end