$basic = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("dungcony:dungcony"))
$token = (Invoke-RestMethod -Method Post "https://tmdt.dungcony.io.vn/vqr/api/token_generate" -Headers @{ Authorization = "Basic $basic" }).access_token

$body = @{
  bankaccount = "0345883926"
  amount = 5000
  transType = "C"
  content = "ORD43847749B13C"
  orderId = "ORD43847749B13C"
  transactionid = "FT26139859508930"
  referencenumber = "FT26139859508930"
  transactiontime = 1779147480000
} | ConvertTo-Json

Invoke-RestMethod -Method Post "https://tmdt.dungcony.io.vn/vqr/bank/api/transaction-sync" `
  -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } `
  -Body $body