# Integração com API de Imagens - Unsplash

## 🖼️ Como Configurar

A aplicação agora busca automaticamente imagens para as criaturas usando a API do Unsplash.

### Passos para Ativar:

1. **Crie uma conta no Unsplash Developers**
   - Acesse: https://unsplash.com/developers
   - Clique em "Register as a developer"

2. **Crie uma nova aplicação**
   - Vá para: https://unsplash.com/oauth/applications
   - Clique em "New Application"
   - Aceite os termos
   - Preencha o nome e descrição da aplicação

3. **Copie sua Access Key**
   - Na página da sua aplicação, copie a "Access Key"

4. **Configure no projeto**
   - Abra o arquivo: `src/main/resources/application-dev.yml`
   - Substitua `your_unsplash_access_key` pela sua chave:
   ```yaml
   unsplash:
     api:
       key: SUA_CHAVE_AQUI
       url: https://api.unsplash.com
   ```

## 📝 Como Funciona

- Quando você **criar ou editar** uma criatura, o sistema automaticamente:
  1. Busca no Unsplash por: "nome da criatura + tipo"
  2. Se não encontrar, busca apenas pelo tipo
  3. Salva a URL da imagem no banco de dados

- A imagem é buscada automaticamente apenas se o campo `imagemUrl` estiver vazio
- Você também pode definir manualmente uma URL de imagem se preferir

## 🎯 Exemplo

Se você criar uma criatura:
- **Nome**: "Dragão Flamejante"
- **Tipo**: "Fogo"

O sistema buscará por: "Dragão Flamejante Fogo" → imagem relacionada

## ⚠️ Importante

- Sem a chave configurada, o sistema funciona normalmente mas não busca imagens
- O Unsplash tem limite de 50 requisições por hora no plano gratuito
- As imagens são de alta qualidade e livres de direitos autorais
